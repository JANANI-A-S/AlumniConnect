-- ==============================================================================
-- Centralized Alumni Tracking & Institutional Engagement Portal - Seed Data
-- ==============================================================================

-- 1. Chart of Accounts Master
-- Assets (Endowment Cash & Bank, Investments)
-- Liabilities (Vendor Payables)
-- Income (Alumni Donations Revenue, Event Receipts)
-- Expenses (Alumni Event & Campaign Expenses, Marketing & Outreach)

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (1, '1010', 'Endowment Cash & Bank', 'ASSET', 87500.00, 'Primary university endowment fund bank account')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (2, '1020', 'Endowment Term Investments', 'ASSET', 250000.00, 'Fixed institutional long-term endowment investments')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (3, '2010', 'Vendor Payables', 'LIABILITY', 12000.00, 'Pending payables owed to gala and marketing vendors')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (4, '4010', 'Alumni Donations Revenue', 'INCOME', 87500.00, 'Cumulative donations received for university endowments')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (5, '4020', 'Alumni Gala Event Receipts', 'INCOME', 15000.00, 'Ticket and sponsorship receipts from institutional galas')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (6, '5010', 'Alumni Event & Campaign Expenses', 'EXPENSE', 12000.00, 'Operational expenses for alumni galas and campaign rallies')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO chart_of_accounts (id, code, name, type, balance, description)
VALUES (7, '5020', 'Marketing & Outreach Expenses', 'EXPENSE', 4500.00, 'Campaign outreach, digital mailers, and print publications')
ON DUPLICATE KEY UPDATE name=VALUES(name);


-- 2. Journal Master
-- Grants/Donations Journal, Purchase Journal, Bank/Cash Journals

INSERT INTO journals (id, code, name, type, description)
VALUES (1, 'JRN-DONATION', 'Grants & Donations Journal', 'GRANTS_DONATIONS', 'Journal for recording received alumni donations and grant funds')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO journals (id, code, name, type, description)
VALUES (2, 'JRN-PURCHASE', 'Purchase Journal', 'PURCHASE', 'Journal for recording vendor bills, procurement, and accounts payable')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO journals (id, code, name, type, description)
VALUES (3, 'JRN-BANK', 'Bank & Cash Journal', 'BANK_CASH', 'Journal for university cash, wire disbursements, and bank reconciliation')
ON DUPLICATE KEY UPDATE name=VALUES(name);


-- 3. Contact Master
-- Donor, Vendor, Agency

INSERT INTO contacts (id, name, email, phone, organization, contact_type, created_at)
VALUES (1, 'Dr. Eleanor Vance', 'eleanor.vance@mit-alum.edu', '+1-617-555-0199', 'Vance Tech Ventures', 'DONOR', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO contacts (id, name, email, phone, organization, contact_type, created_at)
VALUES (2, 'Gala Gourmet Catering Co.', 'contact@galagourmet.com', '+1-212-555-0144', 'Gourmet Hospitality LLC', 'VENDOR', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO contacts (id, name, email, phone, organization, contact_type, created_at)
VALUES (3, 'Horizon Media Agency', 'campaigns@horizonmedia.org', '+1-415-555-0182', 'Horizon Global Communications', 'AGENCY', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);


-- 4. Product Master
-- Endowment Contribution (Service), Alumni Gala Ticket (Service), Campus Mentorship Sponsorship (Service)

INSERT INTO products (id, name, product_type, price, description)
VALUES (1, 'Endowment Contribution', 'ENDOWMENT_CONTRIBUTION', 5000.00, 'Direct institutional contribution to university capital endowment')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO products (id, name, product_type, price, description)
VALUES (2, 'Alumni Gala Ticket', 'GALA_TICKET', 250.00, 'Ticket entry for annual alumni black-tie benefit dinner and gala')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO products (id, name, product_type, price, description)
VALUES (3, 'Campus Mentorship Sponsorship', 'MENTORSHIP_SPONSORSHIP', 1500.00, 'Semester-long sponsorship funding student-alumni mentorship programs')
ON DUPLICATE KEY UPDATE name=VALUES(name);


-- 5. Campaigns
-- 2026 Library Science Expansion Campaign, STEM Research Endowment 2026

INSERT INTO campaigns (id, name, description, target_amount, collected_amount, start_date, end_date, status)
VALUES (1, '2026 Library Science Expansion Campaign', 'Funding state-of-the-art digital archives, quiet study chambers, and AI research labs', 250000.00, 55000.00, '2026-01-01', '2026-12-31', 'ACTIVE')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO campaigns (id, name, description, target_amount, collected_amount, start_date, end_date, status)
VALUES (2, 'STEM Research & Innovation Endowment', 'Endowed professorships and advanced lab equipment for student discovery', 500000.00, 32500.00, '2026-02-15', '2027-02-15', 'ACTIVE')
ON DUPLICATE KEY UPDATE name=VALUES(name);


-- 6. Alumni Master Records with Cumulative Donated Total & Ranking Tiers
-- PLATINUM: >= 50,000, GOLD: >= 20,000, SILVER: >= 5,000, BRONZE: < 5,000

INSERT INTO alumni (id, name, email, batch, contact_number, donation_total, ranking_tier, created_at)
VALUES (1, 'Dr. Eleanor Vance', 'eleanor.vance@mit-alum.edu', 'Class of 2010', '+1-617-555-0199', 55000.00, 'PLATINUM', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO alumni (id, name, email, batch, contact_number, donation_total, ranking_tier, created_at)
VALUES (2, 'Marcus Aurelius Sterling', 'marcus.sterling@alum.org', 'Class of 2014', '+1-312-555-0177', 25000.00, 'GOLD', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO alumni (id, name, email, batch, contact_number, donation_total, ranking_tier, created_at)
VALUES (3, 'Sarah Jenkins', 'sarah.jenkins@alum.org', 'Class of 2018', '+1-206-555-0122', 7500.00, 'SILVER', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO alumni (id, name, email, batch, contact_number, donation_total, ranking_tier, created_at)
VALUES (4, 'David Zhao', 'david.zhao@alum.org', 'Class of 2021', '+1-408-555-0133', 1200.00, 'BRONZE', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO alumni (id, name, email, batch, contact_number, donation_total, ranking_tier, created_at)
VALUES (5, 'Priya Patel', 'priya.patel@alum.org', 'Class of 2023', '+1-646-555-0144', 0.00, 'BRONZE', NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);


-- 7. Campaign Budgets

INSERT INTO budgets (id, campaign_id, target_amount, actual_amount, allocated_expense_budget, actual_expenses, notes)
VALUES (1, 1, 250000.00, 55000.00, 37500.00, 12000.00, '2026 Library Science Expansion Budget with 15% marketing cap')
ON DUPLICATE KEY UPDATE target_amount=VALUES(target_amount);

INSERT INTO budgets (id, campaign_id, target_amount, actual_amount, allocated_expense_budget, actual_expenses, notes)
VALUES (2, 2, 500000.00, 32500.00, 75000.00, 0.00, 'STEM Endowment initial phase')
ON DUPLICATE KEY UPDATE target_amount=VALUES(target_amount);


-- 8. Double-Entry Journal Entries (Initial Seed Records)
-- Debit Bank (1), Credit Donation Revenue (4) for $55,000 donation

INSERT INTO journal_entries (id, journal_id, debit_account_id, credit_account_id, amount, entry_date, description, reference_id, campaign_id)
VALUES (1, 1, 1, 4, 55000.00, NOW(), 'Foundational endowment donation from Dr. Eleanor Vance', 'REC-2026-INIT01', 1)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);

INSERT INTO journal_entries (id, journal_id, debit_account_id, credit_account_id, amount, entry_date, description, reference_id, campaign_id)
VALUES (2, 1, 1, 4, 25000.00, NOW(), 'STEM Innovation endowment gift from Marcus Sterling', 'REC-2026-INIT02', 2)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);

INSERT INTO journal_entries (id, journal_id, debit_account_id, credit_account_id, amount, entry_date, description, reference_id, campaign_id)
VALUES (3, 1, 1, 4, 7500.00, NOW(), 'Library Science contribution from Sarah Jenkins', 'REC-2026-INIT03', 1)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);

-- Debit Alumni Event Expense (6), Credit Vendor Payables (3) for $12,000 bill
INSERT INTO journal_entries (id, journal_id, debit_account_id, credit_account_id, amount, entry_date, description, reference_id, campaign_id)
VALUES (4, 2, 6, 3, 12000.00, NOW(), 'Gala catering and venue setup invoice from Gala Gourmet Catering', 'BILL-2026-INIT01', 1)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);


-- 9. Donations Sample
INSERT INTO donations (id, alumni_id, campaign_id, product_id, amount, donation_date, payment_method, receipt_number, notes, journal_entry_id)
VALUES (1, 1, 1, 1, 55000.00, NOW(), 'BANK_TRANSFER', 'REC-2026-INIT01', 'Pledge fulfilled for Library Science Expansion', 1)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);

INSERT INTO donations (id, alumni_id, campaign_id, product_id, amount, donation_date, payment_method, receipt_number, notes, journal_entry_id)
VALUES (2, 2, 2, 1, 25000.00, NOW(), 'BANK_TRANSFER', 'REC-2026-INIT02', 'STEM Research Endowed Gift', 2)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);

INSERT INTO donations (id, alumni_id, campaign_id, product_id, amount, donation_date, payment_method, receipt_number, notes, journal_entry_id)
VALUES (3, 3, 1, 1, 7500.00, NOW(), 'CREDIT_CARD', 'REC-2026-INIT03', 'Annual Alumni Giving Fund', 3)
ON DUPLICATE KEY UPDATE amount=VALUES(amount);


-- 10. Purchase Orders, Vendor Bills & Payments
INSERT INTO purchase_orders (id, po_number, vendor_id, campaign_id, amount, order_date, status, description, created_at)
VALUES (1, 'PO-2026-001', 2, 1, 12000.00, NOW(), 'BILLED', 'Catering & banquet service for 2026 Library Science Launch Gala', NOW())
ON DUPLICATE KEY UPDATE amount=VALUES(amount);

INSERT INTO vendor_bills (id, bill_number, po_id, vendor_id, campaign_id, amount, bill_date, due_date, status, notes, created_at)
VALUES (1, 'BILL-2026-INIT01', 1, 2, 1, 12000.00, NOW(), '2026-10-31', 'UNPAID', 'Net 30 catering invoice', NOW())
ON DUPLICATE KEY UPDATE amount=VALUES(amount);
