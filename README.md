# Centralized Alumni Tracking & Institutional Engagement Portal

An institutional engagement platform managing alumni records, endowment campaign donations, double-entry financial ledgers, and university development budgets.

Built with **Java 17 / 21**, **Spring Boot 3.2.x**, **Spring Data JPA**, **Spring Security (HTTP Basic & Role-Based Auth)**, **MySQL**, **Maven**, and **Thymeleaf**.

---

## 🏛️ System Architecture & Primary Actors

| Role | Actor | Primary Capabilities |
| :--- | :--- | :--- |
| **`ADMIN`** | Alumni Relations Director | Configures endowment campaigns, reviews contributor rankings, registers alumni, audits full financial accounts |
| **`INVOICING_USER`** | Endowment Accountant | Issues donation receipts, creates vendor purchase orders, processes vendor bills, registers bank disbursements, views reports |
| **`ALUMNI_CONTACT`** | Alumni Donor / Member | Contributes donations, views donation receipts, browses active campaigns, views alumni rankings |
| **`SYSTEM`** | Core Engine | Computes cumulative donation rankings, generates double-entry general ledger entries, tracks campaign budget variances |

---

## 📑 Master Data & Double-Entry Accounting Model

### 1. Master Data Modules
- **Contact Master**: Alumni Donor (`DONOR`), Event Catering Vendor (`VENDOR`), Marketing Agency (`AGENCY`).
- **Product Master**: Endowment Contribution (`ENDOWMENT_CONTRIBUTION`), Alumni Gala Ticket (`GALA_TICKET`), Campus Mentorship Sponsorship (`MENTORSHIP_SPONSORSHIP`).
- **Chart of Accounts (CoA)**:
  - **Assets (`ASSET`)**: `1010 - Endowment Cash & Bank`, `1020 - Endowment Term Investments`
  - **Liabilities (`LIABILITY`)**: `2010 - Vendor Payables`
  - **Income (`INCOME`)**: `4010 - Alumni Donations Revenue`, `4020 - Alumni Gala Event Receipts`
  - **Expenses (`EXPENSE`)**: `5010 - Alumni Event & Campaign Expenses`, `5020 - Marketing & Outreach Expenses`
- **Journals**:
  - `JRN-DONATION` (Grants & Donations Journal)
  - `JRN-PURCHASE` (Purchase Journal)
  - `JRN-BANK` (Bank & Cash Journal)

### 2. Transaction & Ledger Flows
- **Donation Received (`POST /api/donations`)**:
  - Auto-creates double-entry: **Debit** `1010 Endowment Cash & Bank` | **Credit** `4010 Alumni Donations Revenue`
  - Updates Alumni cumulative `donationTotal`
  - Dynamically recalculates Ranking Tier:
    - 💎 **PLATINUM**: $\ge \$50,000$
    - 🥇 **GOLD**: $\ge \$20,000$
    - 🥈 **SILVER**: $\ge \$5,000$
    - 🥉 **BRONZE**: $<\$5,000$
  - Updates Campaign's `collectedAmount` and actual budget collections.
- **Vendor Bill (`POST /api/vendor-bills`)**:
  - Auto-creates double-entry: **Debit** `5010 Alumni Event Expenses` | **Credit** `2010 Vendor Payables` in Purchase Journal.
- **Vendor Payment (`POST /api/payments`)**:
  - Auto-creates double-entry: **Debit** `2010 Vendor Payables` | **Credit** `1010 Endowment Cash & Bank` in Bank Journal.

### 3. Financial & Analytic Reports
- **Balance Sheet (`GET /api/reports/balance-sheet`)**: Endowment fund bank balances & investments vs. pending campaign payables ($Assets = Liabilities + Equity$).
- **Profit & Loss Account (`GET /api/reports/profit-loss`)**: Donation revenues minus campaign marketing and event costs.
- **Campaign Budget Analytics (`GET /api/reports/budget`)**: Target collections vs actual collections, fundraising expenses, net contributions, and target variance.

---

## 🚀 Setup & Execution Guide

### Prerequisites
1. **Java JDK 17** or **21+** (`java -version`)
2. **Apache Maven 3.8+** (`mvn -version`)
3. **MySQL Server 8.0+** running on `localhost:3306`

---

### Step 1: Create Database Schema in MySQL Workbench
Open **MySQL Workbench** or your MySQL command line client and execute:

```sql
CREATE DATABASE IF NOT EXISTS alumni_portal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

### Step 2: Configure Database Credentials
Edit [`src/main/resources/application.properties`](file:///c:/Users/DELL/OneDrive/Desktop/DBMS%20java%20project/src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/alumni_portal_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

*(You can also set environment variables `DB_USERNAME` and `DB_PASSWORD` without changing the file).*

---

### Step 3: Build & Run the Application
Open a terminal in the project root directory:

```bash
# 1. Run unit & integration tests
mvn test

# 2. Start the Spring Boot application
mvn spring-boot:run
```

Once started, the application will be live at:
- **Web Dashboard UI**: [http://localhost:8080](http://localhost:8080) or [http://localhost:8080/dashboard](http://localhost:8080/dashboard)
- **REST Endpoints**: `http://localhost:8080/api/*`

---

## 🔐 Built-in Test Credentials (HTTP Basic Auth)

| Username | Password | Role | Description |
| :--- | :--- | :--- | :--- |
| `admin` | `admin123` | `ROLE_ADMIN` | Alumni Relations Director (Full Access) |
| `accountant` | `accountant123` | `ROLE_INVOICING_USER` | Endowment Accountant (Billing, Payments, Ledgers, Reports) |
| `alumni_user` | `alumni123` | `ROLE_ALUMNI_CONTACT` | Alumni Contributor (Donate, Search, Receipts) |

---

## 📡 REST API Reference

All responses return standard JSON envelopes:
```json
{
  "success": true,
  "message": "...",
  "data": { ... },
  "timestamp": "2026-09-27T13:45:00"
}
```

### 1. Alumni Management
- `POST /api/alumni` - Register a new alumni profile
- `GET /api/alumni` - List all alumni ordered by donation total (descending)
- `GET /api/alumni/{id}` - Get alumni profile by ID
- `GET /api/alumni/search?query={keyword}` - Search alumni by name, email, or batch and compute ranking tiers

### 2. Campaigns
- `GET /api/campaigns` - List all endowment campaigns
- `GET /api/campaigns/{id}` - Get campaign details with percentage achieved
- `POST /api/campaigns` - Create a new endowment campaign

### 3. Donations & Accounting
- `POST /api/donations` - Record donation, auto-create formal receipt, update alumni total & ranking, and auto-post double-entry ledger entry
- `GET /api/donations` - List all donations
- `GET /api/donations/{id}` - Get donation receipt by ID
- `GET /api/donations/receipt/{receiptNumber}` - Get formal receipt by receipt number (e.g. `REC-2026-INIT01`)
- `GET /api/donations/alumni/{alumniId}` - Get alumni's donation history

### 4. Financial Reports
- `GET /api/reports/balance-sheet` - Endowment Balance Sheet (Assets vs Payables & Equity)
- `GET /api/reports/profit-loss` - Profit & Loss Account (Revenues vs Costs)
- `GET /api/reports/budget` - Campaign Budget Report (Target vs Actual Net Collections)

### 5. Procurement & Payables
- `POST /api/purchase-orders` - Create a purchase order
- `GET /api/purchase-orders` - List all purchase orders
- `POST /api/vendor-bills` - Record vendor bill & post liability to Purchase Journal
- `GET /api/vendor-bills` - List vendor bills
- `POST /api/payments` - Pay vendor bill & post disbursement to Bank Journal
- `GET /api/payments` - List disbursements

### 6. Master Data & Ledgers
- `GET /api/chart-of-accounts` - List all CoA accounts & current balances
- `POST /api/chart-of-accounts` - Add account to CoA
- `GET /api/journals` - List journals
- `GET /api/journal-entries` - View full double-entry General Ledger audit log
- `GET /api/contacts` / `POST /api/contacts` - Contacts (Donors, Vendors, Agencies)
- `GET /api/products` / `POST /api/products` - Products (Endowment Contribution, Gala Ticket, Sponsorship)

---

## 📬 Postman Collection

A complete Postman Collection is included in the project root:
- [`Centralized_Alumni_Portal_Postman_Collection.json`](file:///c:/Users/DELL/OneDrive/Desktop/DBMS%20java%20project/Centralized_Alumni_Portal_Postman_Collection.json)

### How to Import & Use:
1. Open **Postman** -> Click **Import** -> Select `Centralized_Alumni_Portal_Postman_Collection.json`.
2. The collection includes pre-configured collection variables:
   - `baseUrl`: `http://localhost:8080`
   - Basic Auth credentials configured for `admin`, `accountant`, and `alumni_user`.
3. All requests contain sample JSON payloads ready to send.

---

## 🧪 Testing with cURL

### 1. Search Alumni (Filtered & Ranked by Donation Total)
```bash
curl -u admin:admin123 http://localhost:8080/api/alumni/search?query=Vance
```

### 2. Record Donation (Auto-Generates Double-Entry Ledger)
```bash
curl -X POST http://localhost:8080/api/donations \
  -u accountant:accountant123 \
  -H "Content-Type: application/json" \
  -d '{
    "alumniId": 1,
    "campaignId": 1,
    "amount": 10000.00,
    "paymentMethod": "BANK_TRANSFER",
    "notes": "Endowment expansion contribution"
  }'
```

### 3. Generate Endowment Balance Sheet
```bash
curl -u admin:admin123 http://localhost:8080/api/reports/balance-sheet
```

### 4. Generate Campaign Budget Analytics
```bash
curl -u admin:admin123 http://localhost:8080/api/reports/budget
```
