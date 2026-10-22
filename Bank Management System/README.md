# 🖥️ Apex Java Desktop ATM Terminal
### *Hardware Kiosk Simulator & Direct JDBC Transaction Engine*

[![Java](https://img.shields.io/badge/Java-17%2B_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![AWT/Swing](https://img.shields.io/badge/GUI-Java_Swing-blue?style=for-the-badge)](https://docs.oracle.com/javase/8/docs/technotes/guides/swing/)
[![JDBC](https://img.shields.io/badge/Driver-MySQL_Connector%2FJ-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://dev.mysql.com/doc/connector-j/en/)

> [!NOTE]
> This directory houses the standalone **Java Swing Desktop Hardware ATM Client**. For the comprehensive distributed architecture, cloud web portal, REST API reference, and database schema, see the [Root Engineering Documentation](../README.md).

---

## 🏛️ Module Overview

The Java Desktop application simulates a physical Automated Teller Machine (ATM) hardware terminal. It establishes high-speed, direct JDBC connections over TLS to the shared **Aiven Cloud MySQL** cluster, executing atomic updates across identity profiles, credential vaults, and the transaction journal.

### Key Capabilities
- **Direct Hardware Simulation**: Embedded pixel-perfect ATM terminal skin (`atm.jpg`) with native AWT event listeners.
- **3-Step Customer KYC Onboarding**:
  - `Page.java`: Personal identity demographics.
  - `Signupone.java`: Financial profile, tax identifiers (PAN, Aadhaar), and income tiers.
  - `Signuptwo.java`: ATM card provisioning, PIN assignment, and facility access permissions.
- **Transactional Operations**:
  - `deposit.java`: Cash ingestion and immediate ledger credit.
  - `withdrawal.java`: Debit processing with real-time balance validation.
  - `Fastcash.java`: 1-click cash dispensing presets ($100 to $10,000).
  - `Pinchange.java`: Multi-table atomic PIN synchronization across `login`, `bank`, and `signuptwo`.
  - `BalanceEnquiry.java`: Real-time aggregated ledger calculation.
  - `MiniStatement.java`: Printable customer transaction journal.

---

## 🛠️ Configuration & Dependencies

The desktop client requires the following libraries located in this directory:
- **`jcalendar-1.4.jar`**: Date picker widget for date-of-birth input.
- **`lib/mysql-connector-j-26.7.0/mysql-connector-j-26.7.0.jar`**: High-performance JDBC driver.

### Database Credentials (`conn.java`)
The connection manager automatically attempts to read:
1. Environment variables (`DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD`, `DB_NAME`, `DB_SSL`).
2. `db.properties` located in the root directory.
3. Fallback local development server on port `3306`.

---

## 🚀 Compilation & Execution

### 1. Compiling Sources
```bash
javac -cp ".;jcalendar-1.4.jar;lib/mysql-connector-j-26.7.0/mysql-connector-j-26.7.0.jar" *.java
```

### 2. Launching the ATM Terminal
```bash
java -cp ".;jcalendar-1.4.jar;lib/mysql-connector-j-26.7.0/mysql-connector-j-26.7.0.jar" Login
```

### 3. Packaging into a Standalone Executable Fat JAR
```powershell
# Create manifest
@"
Main-Class: Login
Class-Path: jcalendar-1.4.jar lib/mysql-connector-j-26.7.0/mysql-connector-j-26.7.0.jar
"@ | Out-File -Encoding ASCII manifest.txt

# Package binary
jar cfm BankATM.jar manifest.txt *.class *.jpg

# Launch
java -jar BankATM.jar
```

---

## 📂 Source Code Manifest

| File | Primary Responsibility |
| :--- | :--- |
| [`Login.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/Login.java) | Card number & PIN authentication window |
| [`Page.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/Page.java) | Step 1: Customer Personal Demographics |
| [`Signupone.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/Signupone.java) | Step 2: KYC, Tax ID & Financial Profile |
| [`Signuptwo.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/Signuptwo.java) | Step 3: Card Issuance & Services Enrollment |
| [`transcation.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/transcation.java) | Main ATM Menu & Action Dispatcher |
| [`deposit.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/deposit.java) | Cash Deposit Transaction Processor |
| [`withdrawal.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/withdrawal.java) | Cash Withdrawal & Overdraft Guard |
| [`Fastcash.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/Fastcash.java) | Preset Fast Cash Dispenser Matrix |
| [`BalanceEnquiry.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/BalanceEnquiry.java) | Real-time Dynamic Balance Calculator |
| [`MiniStatement.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/MiniStatement.java) | Audit Statement Generator |
| [`Pinchange.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/Pinchange.java) | Cross-Table Atomic PIN Manager |
| [`conn.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/conn.java) | Resilient JDBC Connection Broker |
| [`SetupDatabase.java`](file:///c:/Bank%20Management%20System/Bank%20Management%20System/SetupDatabase.java) | DDL Table Provisioning Utility |

---
For system-level architecture and web deployment guides, refer to [**Root README.md**](../README.md).
