package com.alumni.portal.enums;

public enum RankingTier {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM;

    public static RankingTier calculateTier(java.math.BigDecimal totalDonations) {
        if (totalDonations == null) {
            return BRONZE;
        }
        double amount = totalDonations.doubleValue();
        if (amount >= 50000.0) {
            return PLATINUM;
        } else if (amount >= 20000.0) {
            return GOLD;
        } else if (amount >= 5000.0) {
            return SILVER;
        } else {
            return BRONZE;
        }
    }
}
