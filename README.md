# Bank Management System

A Java Swing & MySQL desktop banking application that provides core banking functionalities including account creation, deposits, withdrawals, fast cash, PIN changes, balance enquiry, and mini statements.

## Features
- **Account Registration**: Multi-step application forms (`Page.java`, `Signupone.java`, `Signuptwo.java`)
- **Login Authentication**: Secure card number and PIN authentication (`Login.java`)
- **Transactions Dashboard**: Quick navigation for transactions (`transcation.java`)
- **Deposit & Withdrawal**: Real-time balance updates (`deposit.java`, `withdrawal.java`, `Fastcash.java`)
- **PIN Management**: Update ATM PIN securely (`Pinchange.java`)
- **Balance & Mini-Statement**: View transactions and current account balance (`BalanceEnquiry.java`, `MiniStatement.java`)

## Database Configuration
The application connects via `conn.java`. It supports configuration through `db.properties` or environment variables:

1. Copy `db.properties.example` to `db.properties`:
   ```properties
   db.host=mysql-2494552c-shivakumarnayak2005-012f.i.aivencloud.com
   db.port=13685
   db.name=defaultdb
   db.user=avnadmin
   db.password=YOUR_PASSWORD
   db.ssl=true
   ```

2. Initialize the database tables by executing `database_setup.sql` or running `SetupDatabase.java`.

## Compiling and Running

Compile the project:
```bash
javac -cp ".;jcalendar-1.4.jar;lib/mysql-connector-j-26.7.0/mysql-connector-j-26.7.0.jar" *.java
```

Run the application:
```bash
java -cp ".;jcalendar-1.4.jar;lib/mysql-connector-j-26.7.0/mysql-connector-j-26.7.0.jar" Login
```
