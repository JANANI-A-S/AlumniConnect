// Centralized Alumni Tracking & Engagement Portal Frontend Logic

document.addEventListener('DOMContentLoaded', () => {
    initTabs();
    initSearch();
    initModals();
    initDonationForm();
    initAlumniForm();
    initThemeToggle();
    initCurrencySelector();
    initDashboardCurrency();
});

// Tab Switching
function initTabs() {
    const buttons = document.querySelectorAll('.nav-btn');
    const tabs = document.querySelectorAll('.tab-content');

    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetId = btn.getAttribute('data-tab');
            if (!targetId) return;

            buttons.forEach(b => b.classList.remove('active'));
            tabs.forEach(t => t.classList.remove('active'));

            btn.classList.add('active');
            const targetTab = document.getElementById(targetId);
            if (targetTab) targetTab.classList.add('active');
        });
    });
}

// Live Search Alumni
function initSearch() {
    const searchInput = document.getElementById('alumniSearchInput');
    const tableBody = document.getElementById('alumniTableBody');

    if (!searchInput || !tableBody) return;

    let debounceTimer;
    searchInput.addEventListener('input', (e) => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            const query = e.target.value.trim();
            fetch(`/api/alumni/search?query=${encodeURIComponent(query)}`)
                .then(res => res.json())
                .then(res => {
                    if (res.success && res.data) {
                        renderAlumniTable(res.data, tableBody);
                    }
                })
                .catch(err => console.error("Search error:", err));
        }, 250);
    });
}

function renderAlumniTable(alumniList, tableBody) {
    if (alumniList.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; padding: 24px; color: var(--text-muted);">No alumni profiles found matching search criteria.</td></tr>`;
        return;
    }

    tableBody.innerHTML = alumniList.map(a => `
        <tr>
            <td style="font-weight: 600;">${escapeHtml(a.name)}</td>
            <td style="color: var(--text-muted);">${escapeHtml(a.email)}</td>
            <td><span style="background: rgba(255,255,255,0.06); padding: 2px 8px; border-radius: 4px; font-size: 11.5px;">${escapeHtml(a.batch)}</span></td>
            <td style="color: var(--text-muted);">${escapeHtml(a.contact || 'N/A')}</td>
            <td style="font-weight: 700; color: #34d399;">$${formatMoney(a.donationTotal)}</td>
            <td><span class="tier-badge tier-${a.rankingTier}">${a.rankingTier}</span></td>
        </tr>
    `).join('');
}

// Modal Logic
function initModals() {
    const donationModal = document.getElementById('donationModal');
    const alumniModal = document.getElementById('alumniModal');

    document.querySelectorAll('.open-donation-modal').forEach(btn => {
        btn.addEventListener('click', () => openModal(donationModal));
    });

    document.querySelectorAll('.open-alumni-modal').forEach(btn => {
        btn.addEventListener('click', () => openModal(alumniModal));
    });

    document.querySelectorAll('.modal-close, .modal-backdrop').forEach(el => {
        el.addEventListener('click', (e) => {
            if (e.target === el) {
                closeAllModals();
            }
        });
    });
}

function openModal(modal) {
    if (modal) {
        modal.classList.add('show');
        const alertBox = modal.querySelector('.modal-alert');
        if (alertBox) alertBox.style.display = 'none';
        const receiptBox = modal.querySelector('.receipt-box');
        if (receiptBox) receiptBox.style.display = 'none';
        const form = modal.querySelector('form');
        if (form) form.style.display = 'block';
    }
}

function closeAllModals() {
    document.querySelectorAll('.modal-backdrop').forEach(m => m.classList.remove('show'));
}

// Donation Submission with Double-Entry Confirmation
function initDonationForm() {
    const form = document.getElementById('donationForm');
    if (!form) return;

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.innerText = 'Processing Double-Entry Ledger...';

        const payload = {
            alumniId: parseInt(document.getElementById('donAlumniSelect').value),
            campaignId: parseInt(document.getElementById('donCampaignSelect').value),
            amount: parseFloat(document.getElementById('donAmount').value),
            paymentMethod: document.getElementById('donMethod').value,
            notes: document.getElementById('donNotes').value
        };

        fetch('/api/donations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Basic ' + btoa('accountant:accountant123')
            },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(res => {
            submitBtn.disabled = false;
            submitBtn.innerText = 'Confirm & Post Donation';
            if (res.success && res.data) {
                form.style.display = 'none';
                const receiptBox = document.getElementById('donationReceiptBox');
                if (receiptBox) {
                    receiptBox.style.display = 'block';
                    receiptBox.innerHTML = `
                        <div style="text-align: center; margin-bottom: 12px;">
                            <span style="font-size: 28px;">🎉</span>
                            <h3 style="color: #34d399; margin-top: 4px;">Donation & Ledger Verified</h3>
                            <p style="font-size: 12px; color: var(--text-muted);">Formal Receipt Number: <strong>${res.data.receiptNumber}</strong></p>
                        </div>
                        <div style="background: rgba(0,0,0,0.3); padding: 12px; border-radius: 6px; font-size: 12.5px; margin-bottom: 12px;">
                            <div><strong>Donor:</strong> ${escapeHtml(res.data.alumniName)} (${escapeHtml(res.data.alumniEmail)})</div>
                            <div><strong>Campaign:</strong> ${escapeHtml(res.data.campaignName)}</div>
                            <div><strong>Amount:</strong> <span style="color:#34d399; font-weight:700;">$${formatMoney(res.data.amount)}</span></div>
                            <div><strong>Method:</strong> ${res.data.paymentMethod}</div>
                        </div>
                        <div style="background: rgba(79, 70, 229, 0.15); border: 1px solid rgba(129, 140, 248, 0.3); padding: 12px; border-radius: 6px; font-size: 12px;">
                            <div style="font-weight: 700; color: #818cf8; margin-bottom: 4px;">Double-Entry Ledger Impact:</div>
                            <div>↳ <span class="ledger-debit">DEBIT:</span> ${res.data.debitAccountName} ($${formatMoney(res.data.amount)})</div>
                            <div>↳ <span class="ledger-credit">CREDIT:</span> ${res.data.creditAccountName} ($${formatMoney(res.data.amount)})</div>
                            <div style="font-size: 11px; color: var(--text-muted); margin-top: 4px;">Journal Entry #${res.data.journalEntryId} auto-posted to Grants/Donations Journal.</div>
                        </div>
                        <button class="btn btn-outline" style="width:100%; margin-top: 14px;" onclick="window.location.reload();">Refresh Portal Data</button>
                    `;
                }
            } else {
                alert('Error processing donation: ' + (res.message || 'Unknown error'));
            }
        })
        .catch(err => {
            submitBtn.disabled = false;
            submitBtn.innerText = 'Confirm & Post Donation';
            alert('Failed to process donation: ' + err.message);
        });
    });
}

// Alumni Registration Submission
function initAlumniForm() {
    const form = document.getElementById('alumniForm');
    if (!form) return;

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.disabled = true;

        const payload = {
            name: document.getElementById('alumName').value,
            email: document.getElementById('alumEmail').value,
            batch: document.getElementById('alumBatch').value,
            contact: document.getElementById('alumContact').value
        };

        fetch('/api/alumni', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Basic ' + btoa('admin:admin123')
            },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(res => {
            submitBtn.disabled = false;
            if (res.success) {
                alert('Alumni ' + res.data.name + ' successfully registered!');
                window.location.reload();
            } else {
                alert('Registration failed: ' + (res.message || 'Error'));
            }
        })
        .catch(err => {
            submitBtn.disabled = false;
            alert('Error registering alumni: ' + err.message);
        });
    });
}

function formatMoney(amount) {
    if (!amount) return '0.00';
    return Number(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function initThemeToggle() {
    const toggleButton = document.getElementById('themeToggle');
    if (!toggleButton) return;

    const savedTheme = localStorage.getItem('alumni-theme') || 'theme-light';
    document.body.classList.add(savedTheme);
    toggleButton.textContent = savedTheme === 'theme-dark' ? '☀️' : '🌙';

    toggleButton.addEventListener('click', () => {
        const isDark = document.body.classList.contains('theme-dark');
        document.body.classList.remove('theme-dark', 'theme-light');
        const nextTheme = isDark ? 'theme-light' : 'theme-dark';
        document.body.classList.add(nextTheme);
        localStorage.setItem('alumni-theme', nextTheme);
        toggleButton.textContent = nextTheme === 'theme-dark' ? '☀️' : '🌙';
    });
}

function initCurrencySelector() {
    const select = document.getElementById('currencySelect');
    const symbolTarget = document.getElementById('currencySymbol');
    const amountTarget = document.getElementById('currencyAmount');
    if (!select || !symbolTarget || !amountTarget) return;

    const currencyMap = {
        USD: { symbol: '$', amount: '2,500.00', rate: 1 },
        EUR: { symbol: '€', amount: '2,300.00', rate: 0.92 },
        GBP: { symbol: '£', amount: '2,050.00', rate: 0.82 },
        INR: { symbol: '₹', amount: '2,10,000.00', rate: 83.3 },
        JPY: { symbol: '¥', amount: '370,000.00', rate: 157 },
        AED: { symbol: 'د.إ', amount: '9,200.00', rate: 3.67 },
        AUD: { symbol: 'A$', amount: '3,700.00', rate: 1.5 },
        CAD: { symbol: 'C$', amount: '3,400.00', rate: 1.36 },
        SGD: { symbol: 'S$', amount: '3,300.00', rate: 1.35 },
        CNY: { symbol: '¥', amount: '18,000.00', rate: 7.24 }
    };

    const update = () => {
        const selected = currencyMap[select.value] || currencyMap.USD;
        symbolTarget.textContent = selected.symbol;
        amountTarget.textContent = selected.amount;
    };

    select.addEventListener('change', update);
    update();
}

function initDashboardCurrency() {
    const select = document.getElementById('dashboardCurrencySelect');
    if (!select) return;

    const currencyMap = {
        USD: { symbol: '$', rate: 1 },
        INR: { symbol: '₹', rate: 83.3 },
        EUR: { symbol: '€', rate: 0.92 },
        GBP: { symbol: '£', rate: 0.82 },
        JPY: { symbol: '¥', rate: 157 },
        AED: { symbol: 'د.إ', rate: 3.67 },
        AUD: { symbol: 'A$', rate: 1.5 },
        CAD: { symbol: 'C$', rate: 1.36 },
        SGD: { symbol: 'S$', rate: 1.35 },
        CNY: { symbol: '¥', rate: 7.24 }
    };

    const formatCurrency = (value, symbol) => {
        const formatted = Number(value).toLocaleString('en-US', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
        return `${symbol}${formatted}`;
    };

    const updateValues = () => {
        const selected = currencyMap[select.value] || currencyMap.USD;
        const amountNodes = document.querySelectorAll('.currency-value');

        amountNodes.forEach(node => {
            const baseValue = Number(node.dataset.currencyValue || 0);
            const updated = baseValue * selected.rate;
            node.textContent = formatCurrency(updated, selected.symbol);
        });
    };

    select.addEventListener('change', updateValues);
    updateValues();
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}
