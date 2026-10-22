const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');
const mysql = require('mysql2/promise');

const PORT = process.env.PORT || 3000;
const PUBLIC_DIR = path.join(__dirname, 'public');

// Load credentials from db.properties if exists locally
function loadLocalDbProperties() {
  const props = {};
  try {
    const propPath = path.join(__dirname, 'db.properties');
    if (fs.existsSync(propPath)) {
      const content = fs.readFileSync(propPath, 'utf8');
      content.split(/\r?\n/).forEach(line => {
        const trimmed = line.trim();
        if (trimmed && !trimmed.startsWith('#')) {
          const idx = trimmed.indexOf('=');
          if (idx !== -1) {
            props[trimmed.substring(0, idx).trim()] = trimmed.substring(idx + 1).trim();
          }
        }
      });
    }
  } catch (_) {}
  return props;
}

const localProps = loadLocalDbProperties();

// Database pool configured for Aiven Cloud MySQL
const pool = mysql.createPool({
  host: process.env.DB_HOST || localProps['db.host'] || 'mysql-2494552c-shivakumarnayak2005-012f.i.aivencloud.com',
  port: parseInt(process.env.DB_PORT || localProps['db.port'] || '13685', 10),
  user: process.env.DB_USER || localProps['db.user'] || 'avnadmin',
  password: process.env.DB_PASSWORD || localProps['db.password'] || '',
  database: process.env.DB_NAME || localProps['db.name'] || 'defaultdb',
  ssl: { rejectUnauthorized: false },
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// Test connection on startup
(async () => {
  try {
    const conn = await pool.getConnection();
    console.log('Connected to MySQL database on Aiven successfully.');
    conn.release();
  } catch (err) {
    console.error('Initial database connection warning:', err.message);
  }
})();

const mimeTypes = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

const server = http.createServer(async (req, res) => {
  const parsedUrl = url.parse(req.url, true);
  const pathname = parsedUrl.pathname;

  // JSON helper
  const sendJson = (statusCode, data) => {
    res.writeHead(statusCode, {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type'
    });
    res.end(JSON.stringify(data));
  };

  if (req.method === 'OPTIONS') {
    res.writeHead(204, {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type'
    });
    return res.end();
  }

  // Parse JSON Body for POST requests
  let body = {};
  if (req.method === 'POST') {
    try {
      const buffers = [];
      for await (const chunk of req) {
        buffers.push(chunk);
      }
      const rawBody = Buffer.concat(buffers).toString();
      body = rawBody ? JSON.parse(rawBody) : {};
    } catch (e) {
      body = {};
    }
  }

  // API ROUTES
  try {
    // 1. Health check
    if (pathname === '/api/health') {
      return sendJson(200, { status: 'ok', timestamp: new Date().toISOString() });
    }

    // 2. Login Authentication
    if (pathname === '/api/login' && req.method === 'POST') {
      const { cardNumber, pin } = body;
      if (!cardNumber || !pin) {
        return sendJson(400, { success: false, message: 'Card number and PIN are required' });
      }

      const cleanCard = String(cardNumber).replace(/\s/g, '');
      const [rows] = await pool.query(
        'SELECT formno, cardnumber, pin FROM login WHERE cardnumber = ? AND pin = ?',
        [cleanCard, String(pin)]
      );

      if (rows.length > 0) {
        // Fetch user name if available in signup table
        let userName = 'Customer';
        try {
          const [uRows] = await pool.query('SELECT name FROM signup WHERE formno = ?', [rows[0].formno]);
          if (uRows.length > 0 && uRows[0].name) {
            userName = uRows[0].name;
          }
        } catch (_) {}

        return sendJson(200, {
          success: true,
          message: 'Login successful',
          cardnumber: rows[0].cardnumber,
          pin: rows[0].pin,
          userName
        });
      } else {
        return sendJson(401, { success: false, message: 'Invalid Card Number or PIN' });
      }
    }

    // 3. Balance Enquiry
    if (pathname === '/api/balance' && req.method === 'GET') {
      const pin = parsedUrl.query.pin;
      if (!pin) {
        return sendJson(400, { success: false, message: 'PIN is required' });
      }

      const [rows] = await pool.query('SELECT type, amount FROM bank WHERE pin = ?', [String(pin)]);
      let balance = 0;
      for (const row of rows) {
        const amt = parseFloat(row.amount) || 0;
        if (String(row.type).toLowerCase() === 'deposit') {
          balance += amt;
        } else {
          balance -= amt;
        }
      }

      return sendJson(200, { success: true, balance });
    }

    // 4. Deposit / Withdrawal / Fast Cash Transaction
    if (pathname === '/api/transaction' && req.method === 'POST') {
      const { pin, type, amount } = body;
      const numAmount = parseFloat(amount);

      if (!pin || !type || isNaN(numAmount) || numAmount <= 0) {
        return sendJson(400, { success: false, message: 'Valid PIN, transaction type, and amount are required' });
      }

      const isDeposit = String(type).toLowerCase() === 'deposit';

      // Check balance if withdrawing
      if (!isDeposit) {
        const [rows] = await pool.query('SELECT type, amount FROM bank WHERE pin = ?', [String(pin)]);
        let currentBalance = 0;
        for (const row of rows) {
          const amt = parseFloat(row.amount) || 0;
          if (String(row.type).toLowerCase() === 'deposit') {
            currentBalance += amt;
          } else {
            currentBalance -= amt;
          }
        }

        if (currentBalance < numAmount) {
          return sendJson(400, {
            success: false,
            message: `Insufficient Funds! Available balance is $${currentBalance.toFixed(2)}`
          });
        }
      }

      const dateStr = new Date().toLocaleString('en-US', {
        month: 'short', day: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true
      });

      const txType = isDeposit ? 'Deposit' : 'withdrawal';
      await pool.query(
        'INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)',
        [String(pin), dateStr, txType, String(numAmount)]
      );

      // Re-calculate new balance
      const [allRows] = await pool.query('SELECT type, amount FROM bank WHERE pin = ?', [String(pin)]);
      let newBalance = 0;
      for (const row of allRows) {
        const amt = parseFloat(row.amount) || 0;
        if (String(row.type).toLowerCase() === 'deposit') {
          newBalance += amt;
        } else {
          newBalance -= amt;
        }
      }

      return sendJson(200, {
        success: true,
        message: `${isDeposit ? 'Deposit' : 'Withdrawal'} of $${numAmount.toFixed(2)} successful!`,
        newBalance
      });
    }

    // 5. Mini Statement
    if (pathname === '/api/ministatement' && req.method === 'GET') {
      const pin = parsedUrl.query.pin;
      if (!pin) {
        return sendJson(400, { success: false, message: 'PIN is required' });
      }

      const [cardRows] = await pool.query('SELECT cardnumber FROM login WHERE pin = ? LIMIT 1', [String(pin)]);
      const cardnumber = cardRows.length > 0 ? cardRows[0].cardnumber : 'Unavailable';

      const [txRows] = await pool.query('SELECT pin, date, type, amount FROM bank WHERE pin = ? ORDER BY date DESC', [String(pin)]);

      let balance = 0;
      for (const row of txRows) {
        const amt = parseFloat(row.amount) || 0;
        if (String(row.type).toLowerCase() === 'deposit') {
          balance += amt;
        } else {
          balance -= amt;
        }
      }

      return sendJson(200, {
        success: true,
        cardnumber,
        balance,
        transactions: txRows
      });
    }

    // 6. PIN Change
    if (pathname === '/api/pinchange' && req.method === 'POST') {
      const { oldPin, newPin } = body;
      if (!oldPin || !newPin) {
        return sendJson(400, { success: false, message: 'Old PIN and new PIN are required' });
      }
      if (String(newPin).length < 4) {
        return sendJson(400, { success: false, message: 'New PIN must be at least 4 digits' });
      }

      const [rows] = await pool.query('SELECT * FROM login WHERE pin = ?', [String(oldPin)]);
      if (rows.length === 0) {
        return sendJson(400, { success: false, message: 'Incorrect Current PIN' });
      }

      await pool.query('UPDATE bank SET pin = ? WHERE pin = ?', [String(newPin), String(oldPin)]);
      await pool.query('UPDATE login SET pin = ? WHERE pin = ?', [String(newPin), String(oldPin)]);
      await pool.query('UPDATE signuptwo SET pin = ? WHERE pin = ?', [String(newPin), String(oldPin)]);

      return sendJson(200, { success: true, message: 'PIN successfully updated!', newPin: String(newPin) });
    }

    // 7. Full 3-Step Account Application (Signup)
    if (pathname === '/api/signup' && req.method === 'POST') {
      const {
        name, fathername, dob, gender, email, martialstatus, address, city, state, pincode,
        religion, category, income, education, occupation, pan, aadhar, seniorcitizen, existingaccount,
        accounttype, facility
      } = body;

      if (!name || !email || !address) {
        return sendJson(400, { success: false, message: 'Name, Email, and Address are required' });
      }

      // Generate unique 4-digit Application form number
      const formno = String(Math.floor(1000 + Math.random() * 9000));
      // Generate 16-digit card number (starting with 5040)
      const random12 = Math.floor(100000000000 + Math.random() * 900000000000);
      const cardnumber = '5040' + String(random12);
      // Generate 4-digit PIN
      const pin = String(Math.floor(1000 + Math.random() * 9000));

      // 1. Insert into signup
      await pool.query(
        'INSERT INTO signup (formno, name, fathername, dob, gender, email, martialstatus, address, city, state, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)',
        [
          formno, name || '', fathername || '', dob || '', gender || 'Male',
          email || '', martialstatus || 'Single', address || '', city || '', state || '', pincode || ''
        ]
      );

      // 2. Insert into signupone
      await pool.query(
        'INSERT INTO signupone (religion, category, income, education, occupation, pan, aadhar, seniorcitizen, existingaccount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)',
        [
          religion || 'General', category || 'General', income || '0', education || 'Graduate',
          occupation || 'Employed', pan || '', aadhar || '', seniorcitizen || 'No', existingaccount || 'No'
        ]
      );

      // 3. Insert into signuptwo
      await pool.query(
        'INSERT INTO signuptwo (formno, accounttype, cardnumber, pin, facility) VALUES (?, ?, ?, ?, ?)',
        [formno, accounttype || 'Saving Account', cardnumber, pin, facility || 'ATM Card, Internet Banking']
      );

      // 4. Insert into login
      await pool.query(
        'INSERT INTO login (formno, cardnumber, pin) VALUES (?, ?, ?)',
        [formno, cardnumber, pin]
      );

      // Initial opening balance of $500 bonus
      const dateStr = new Date().toLocaleString('en-US', {
        month: 'short', day: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true
      });
      await pool.query(
        'INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)',
        [pin, dateStr, 'Deposit', '500']
      );

      return sendJson(200, {
        success: true,
        message: 'Account created successfully with $500 welcome deposit!',
        formno,
        cardnumber,
        pin
      });
    }

  } catch (apiErr) {
    console.error('API Error processing ' + pathname + ':', apiErr);
    return sendJson(500, { success: false, message: 'Database/Server Error: ' + apiErr.message });
  }

  // STATIC FILE ROUTING
  let filePath = path.join(PUBLIC_DIR, pathname === '/' ? 'index.html' : pathname);

  if (!path.extname(filePath)) {
    if (fs.existsSync(filePath + '.html')) {
      filePath += '.html';
    } else if (fs.existsSync(path.join(filePath, 'index.html'))) {
      filePath = path.join(filePath, 'index.html');
    }
  }

  fs.readFile(filePath, (err, content) => {
    if (err) {
      if (err.code === 'ENOENT') {
        const notFoundPath = path.join(PUBLIC_DIR, 'index.html');
        fs.readFile(notFoundPath, (err2, fallbackContent) => {
          if (err2) {
            res.writeHead(404, { 'Content-Type': 'text/plain' });
            return res.end('404 Not Found');
          }
          res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
          res.end(fallbackContent);
        });
      } else {
        res.writeHead(500, { 'Content-Type': 'text/plain' });
        res.end('500 Server Error: ' + err.code);
      }
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = mimeTypes[ext] || 'application/octet-stream';
    res.writeHead(200, { 'Content-Type': contentType });
    res.end(content);
  });
});

server.listen(PORT, () => {
  console.log(`Apex Bank Management System running on http://localhost:${PORT}`);
});
