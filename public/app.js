// =============================================================
// APEX BANK CLIENT LOGIC & API INTEGRATION
// =============================================================

// Toast Notification Helper
function showToast(message, type = 'info') {
  const container = document.getElementById('toastContainer');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `
    <span>${type === 'success' ? '✅' : type === 'error' ? '❌' : 'ℹ️'}</span>
    <span>${message}</span>
  `;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

// Format number with commas
function formatCurrency(num) {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(num);
}

// -------------------------------------------------------------
// 1. INDEX / LOGIN PAGE LOGIC
// -------------------------------------------------------------
const cardNumberInput = document.getElementById('cardNumber');
const pinNumberInput = document.getElementById('pinNumber');
const loginForm = document.getElementById('loginForm');
const previewCardNumber = document.getElementById('previewCardNumber');
const togglePinBtn = document.getElementById('togglePinBtn');

if (cardNumberInput) {
  // Auto-format card number as 4-4-4-4
  cardNumberInput.addEventListener('input', (e) => {
    let val = e.target.value.replace(/\D/g, '').substring(0, 16);
    let formatted = val.match(/.{1,4}/g)?.join(' ') || val;
    e.target.value = formatted;

    if (previewCardNumber) {
      previewCardNumber.textContent = formatted || '5040 •••• •••• ••••';
    }
  });
}

if (togglePinBtn && pinNumberInput) {
  togglePinBtn.addEventListener('click', () => {
    const isPass = pinNumberInput.type === 'password';
    pinNumberInput.type = isPass ? 'text' : 'password';
    togglePinBtn.textContent = isPass ? '🔒' : '👁️';
  });
}

if (loginForm) {
  loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = document.getElementById('loginSubmitBtn');
    const originalText = btn.innerHTML;
    btn.disabled = true;
    btn.innerHTML = 'Authenticating...';

    const card = cardNumberInput.value.replace(/\s/g, '');
    const pin = pinNumberInput.value.trim();

    try {
      const res = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cardNumber: card, pin })
      });

      const data = await res.json();
      if (data.success) {
        showToast('Access Granted! Redirecting to Dashboard...', 'success');
        localStorage.setItem('apex_user', JSON.stringify({
          cardnumber: data.cardnumber,
          pin: data.pin,
          userName: data.userName
        }));
        setTimeout(() => {
          window.location.href = 'dashboard.html';
        }, 800);
      } else {
        showToast(data.message || 'Invalid Card Number or PIN', 'error');
        btn.disabled = false;
        btn.innerHTML = originalText;
      }
    } catch (err) {
      showToast('Network/Server Error: ' + err.message, 'error');
      btn.disabled = false;
      btn.innerHTML = originalText;
    }
  });
}

// -------------------------------------------------------------
// 2. SIGNUP WIZARD LOGIC
// -------------------------------------------------------------
const toStep2Btn = document.getElementById('toStep2Btn');
const toStep3Btn = document.getElementById('toStep3Btn');
const backToStep1Btn = document.getElementById('backToStep1Btn');
const backToStep2Btn = document.getElementById('backToStep2Btn');
const signupWizardForm = document.getElementById('signupWizardForm');

function setWizardStep(stepNum) {
  [1, 2, 3].forEach(n => {
    const stepEl = document.getElementById(`step${n}`);
    const indicatorEl = document.getElementById(`stepIndicator${n}`);
    if (stepEl) stepEl.classList.toggle('active', n === stepNum);
    if (indicatorEl) {
      indicatorEl.classList.toggle('active', n === stepNum);
      indicatorEl.classList.toggle('completed', n < stepNum);
    }
  });
}

if (toStep2Btn) {
  toStep2Btn.addEventListener('click', () => {
    const name = document.getElementById('fullName').value.trim();
    const email = document.getElementById('email').value.trim();
    const address = document.getElementById('address').value.trim();
    if (!name || !email || !address) {
      showToast('Please fill all mandatory personal details (Name, Email, Address)', 'error');
      return;
    }
    setWizardStep(2);
  });
}

if (backToStep1Btn) {
  backToStep1Btn.addEventListener('click', () => setWizardStep(1));
}

if (toStep3Btn) {
  toStep3Btn.addEventListener('click', () => {
    const income = document.getElementById('income').value.trim();
    const pan = document.getElementById('panNumber').value.trim();
    const aadhar = document.getElementById('aadharNumber').value.trim();
    if (!income || !pan || !aadhar) {
      showToast('Please fill Income, PAN, and Aadhaar numbers', 'error');
      return;
    }
    setWizardStep(3);
  });
}

if (backToStep2Btn) {
  backToStep2Btn.addEventListener('click', () => setWizardStep(2));
}

if (signupWizardForm) {
  signupWizardForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const submitBtn = document.getElementById('submitApplicationBtn');
    submitBtn.disabled = true;
    submitBtn.innerHTML = 'Provisioning Account in Cloud...';

    const accountType = document.querySelector('input[name="accountType"]:checked')?.value || 'Saving Account';
    const facilities = [];
    if (document.getElementById('facAtm')?.checked) facilities.push('ATM Card');
    if (document.getElementById('facNet')?.checked) facilities.push('Internet Banking');
    if (document.getElementById('facAlerts')?.checked) facilities.push('Email & SMS Alerts');
    if (document.getElementById('facCheque')?.checked) facilities.push('Cheque Book');

    const payload = {
      name: document.getElementById('fullName').value.trim(),
      fathername: document.getElementById('fatherName').value.trim(),
      dob: document.getElementById('dob').value,
      gender: document.getElementById('gender').value,
      email: document.getElementById('email').value.trim(),
      martialstatus: document.getElementById('maritalStatus').value,
      address: document.getElementById('address').value.trim(),
      city: document.getElementById('city').value.trim(),
      state: document.getElementById('state').value.trim(),
      pincode: document.getElementById('pincode').value.trim(),
      religion: document.getElementById('religion').value,
      category: document.getElementById('category').value,
      income: document.getElementById('income').value,
      education: document.getElementById('education').value,
      occupation: document.getElementById('occupation').value,
      pan: document.getElementById('panNumber').value.trim(),
      aadhar: document.getElementById('aadharNumber').value.trim(),
      seniorcitizen: document.getElementById('seniorCitizen').value,
      existingaccount: 'No',
      accounttype: accountType,
      facility: facilities.join(', ')
    };

    try {
      const res = await fetch('/api/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      if (data.success) {
        showToast('Account Created Successfully!', 'success');
        signupWizardForm.style.display = 'none';
        const successCard = document.getElementById('signupSuccessCard');
        if (successCard) {
          successCard.style.display = 'block';
          document.getElementById('resFormNo').textContent = '#' + data.formno;
          const formattedCard = data.cardnumber.match(/.{1,4}/g)?.join(' ') || data.cardnumber;
          document.getElementById('resCardNumber').textContent = formattedCard;
          document.getElementById('resPin').textContent = data.pin;
        }
      } else {
        showToast(data.message || 'Account creation failed', 'error');
        submitBtn.disabled = false;
        submitBtn.innerHTML = 'Submit & Generate ATM Card ✨';
      }
    } catch (err) {
      showToast('Error: ' + err.message, 'error');
      submitBtn.disabled = false;
      submitBtn.innerHTML = 'Submit & Generate ATM Card ✨';
    }
  });
}

// -------------------------------------------------------------
// 3. DASHBOARD PAGE LOGIC
// -------------------------------------------------------------
const dashBalanceDisplay = document.getElementById('dashBalanceDisplay');

if (dashBalanceDisplay) {
  const sessionUser = JSON.parse(localStorage.getItem('apex_user') || 'null');
  if (!sessionUser || !sessionUser.pin) {
    window.location.href = 'index.html';
  } else {
    // Populate User Details
    const nameEl = document.getElementById('dashUserName');
    const cardEl = document.getElementById('dashMaskedCard');
    if (nameEl) nameEl.textContent = sessionUser.userName || 'Customer';
    if (cardEl && sessionUser.cardnumber) {
      const c = sessionUser.cardnumber;
      cardEl.textContent = `${c.substring(0, 4)} •••• •••• ${c.substring(c.length - 4)}`;
    }

    // Load Balance and Statements
    syncDashboardData();
  }

  // Logout Button
  const logoutBtn = document.getElementById('logoutBtn');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      localStorage.removeItem('apex_user');
      showToast('Logged out successfully', 'info');
      setTimeout(() => { window.location.href = 'index.html'; }, 500);
    });
  }

  // Refresh Balance Button
  const refreshBalanceBtn = document.getElementById('refreshBalanceBtn');
  if (refreshBalanceBtn) {
    refreshBalanceBtn.addEventListener('click', () => {
      syncDashboardData(true);
    });
  }

  // Modal Setup Helpers
  setupModal('openDepositModalBtn', 'depositModal');
  setupModal('openWithdrawalModalBtn', 'withdrawalModal');
  setupModal('cardActionDeposit', 'depositModal');
  setupModal('cardActionWithdraw', 'withdrawalModal');
  setupModal('cardActionFastCash', 'fastCashModal');
  setupModal('cardActionPinChange', 'pinChangeModal');

  // Handle Deposit Submit
  const depositForm = document.getElementById('depositForm');
  if (depositForm) {
    depositForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const amt = document.getElementById('depositAmount').value;
      await handleTransaction('Deposit', amt, 'depositModal');
    });
  }

  // Handle Withdrawal Submit
  const withdrawalForm = document.getElementById('withdrawalForm');
  if (withdrawalForm) {
    withdrawalForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const amt = document.getElementById('withdrawAmount').value;
      await handleTransaction('withdrawal', amt, 'withdrawalModal');
    });
  }

  // Handle Fast Cash Buttons
  document.querySelectorAll('.fast-cash-btn').forEach(btn => {
    btn.addEventListener('click', async () => {
      const amt = btn.dataset.amt;
      await handleTransaction('withdrawal', amt, 'fastCashModal');
    });
  });

  // Handle PIN Change Submit
  const pinChangeForm = document.getElementById('pinChangeForm');
  if (pinChangeForm) {
    pinChangeForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const currentPin = document.getElementById('currentPin').value.trim();
      const newPin = document.getElementById('newPin').value.trim();
      const confirmPin = document.getElementById('confirmNewPin').value.trim();

      if (newPin !== confirmPin) {
        showToast('New PIN and Confirm PIN do not match!', 'error');
        return;
      }

      const sessionUser = JSON.parse(localStorage.getItem('apex_user') || '{}');
      if (currentPin !== sessionUser.pin) {
        showToast('Current PIN does not match logged-in PIN!', 'error');
        return;
      }

      try {
        const res = await fetch('/api/pinchange', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ oldPin: currentPin, newPin })
        });
        const data = await res.json();
        if (data.success) {
          showToast('PIN successfully changed!', 'success');
          sessionUser.pin = newPin;
          localStorage.setItem('apex_user', JSON.stringify(sessionUser));
          closeModal('pinChangeModal');
          pinChangeForm.reset();
        } else {
          showToast(data.message || 'PIN change failed', 'error');
        }
      } catch (err) {
        showToast('Error: ' + err.message, 'error');
      }
    });
  }

  // Mini Statement Action Scroll
  const cardActionStatement = document.getElementById('cardActionStatement');
  if (cardActionStatement) {
    cardActionStatement.addEventListener('click', () => {
      const table = document.querySelector('.tx-table-wrapper');
      if (table) {
        table.scrollIntoView({ behavior: 'smooth' });
      }
    });
  }
}

async function handleTransaction(type, amount, modalId) {
  const sessionUser = JSON.parse(localStorage.getItem('apex_user') || '{}');
  try {
    const res = await fetch('/api/transaction', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ pin: sessionUser.pin, type, amount })
    });
    const data = await res.json();
    if (data.success) {
      showToast(data.message, 'success');
      closeModal(modalId);
      syncDashboardData();
    } else {
      showToast(data.message || 'Transaction failed', 'error');
    }
  } catch (err) {
    showToast('Network error: ' + err.message, 'error');
  }
}

async function syncDashboardData(showSuccessToast = false) {
  const sessionUser = JSON.parse(localStorage.getItem('apex_user') || '{}');
  if (!sessionUser.pin) return;

  try {
    const res = await fetch(`/api/ministatement?pin=${sessionUser.pin}`);
    const data = await res.json();
    if (data.success) {
      const balEl = document.getElementById('dashBalanceDisplay');
      if (balEl) balEl.textContent = formatCurrency(data.balance);

      const timeEl = document.getElementById('dashLastSyncTime');
      if (timeEl) timeEl.textContent = new Date().toLocaleTimeString();

      // Populate Transactions
      const tbody = document.getElementById('txTableBody');
      if (tbody) {
        if (!data.transactions || data.transactions.length === 0) {
          tbody.innerHTML = `
            <tr>
              <td colspan="4" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                No transaction records found for this account.
              </td>
            </tr>
          `;
        } else {
          tbody.innerHTML = data.transactions.map(tx => {
            const isDeposit = String(tx.type).toLowerCase() === 'deposit';
            const badgeClass = isDeposit ? 'badge-deposit' : 'badge-withdrawal';
            const prefix = isDeposit ? '+' : '-';
            return `
              <tr>
                <td><strong style="color:#fff;">${tx.date || 'N/A'}</strong></td>
                <td><span class="badge ${badgeClass}">${tx.type}</span></td>
                <td style="font-family: var(--font-mono); color: var(--text-muted);">••••</td>
                <td style="text-align: right; font-family: var(--font-mono); font-weight: 700; color: ${isDeposit ? 'var(--accent-emerald)' : 'var(--accent-rose)'};">
                  ${prefix}${formatCurrency(parseFloat(tx.amount) || 0)}
                </td>
              </tr>
            `;
          }).join('');
        }
      }

      if (showSuccessToast) showToast('Balance and ledger synchronized from cloud', 'success');
    }
  } catch (err) {
    console.error('Sync Error:', err);
  }
}

// Modal Helper Functions
function setupModal(triggerId, modalId) {
  const trigger = document.getElementById(triggerId);
  const modal = document.getElementById(modalId);
  if (trigger && modal) {
    trigger.addEventListener('click', () => {
      modal.classList.add('active');
    });
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.classList.remove('active');
}

document.querySelectorAll('[data-close]').forEach(btn => {
  btn.addEventListener('click', () => {
    closeModal(btn.dataset.close);
  });
});

document.querySelectorAll('.modal-overlay').forEach(overlay => {
  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) {
      overlay.classList.remove('active');
    }
  });
});
