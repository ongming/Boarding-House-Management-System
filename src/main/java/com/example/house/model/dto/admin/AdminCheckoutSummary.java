package com.example.house.model.dto.admin;

import java.math.BigDecimal;

public class AdminCheckoutSummary {
    private final BigDecimal deposit;
    private final BigDecimal unpaidInvoices;
    private final BigDecimal unpaidCompensations;
    private final BigDecimal refundAmount;

    public AdminCheckoutSummary(BigDecimal deposit,
                                BigDecimal unpaidInvoices,
                                BigDecimal unpaidCompensations,
                                BigDecimal refundAmount) {
        this.deposit = deposit;
        this.unpaidInvoices = unpaidInvoices;
        this.unpaidCompensations = unpaidCompensations;
        this.refundAmount = refundAmount;
    }

    public BigDecimal deposit() {
        return deposit;
    }

    public BigDecimal unpaidInvoices() {
        return unpaidInvoices;
    }

    public BigDecimal unpaidCompensations() {
        return unpaidCompensations;
    }

    public BigDecimal refundAmount() {
        return refundAmount;
    }
}
