package com.example.house.model.dto.admin;

import java.math.BigDecimal;

public class AdminRevenueRow {
    private final String label;
    private final BigDecimal invoiceTotal;
    private final BigDecimal compensationTotal;

    public AdminRevenueRow(String label, BigDecimal invoiceTotal, BigDecimal compensationTotal) {
        this.label = label;
        this.invoiceTotal = invoiceTotal;
        this.compensationTotal = compensationTotal;
    }

    public String label() {
        return label;
    }

    public BigDecimal invoiceTotal() {
        return invoiceTotal;
    }

    public BigDecimal compensationTotal() {
        return compensationTotal;
    }

    public BigDecimal total() {
        BigDecimal invoice = invoiceTotal == null ? BigDecimal.ZERO : invoiceTotal;
        BigDecimal compensation = compensationTotal == null ? BigDecimal.ZERO : compensationTotal;
        return invoice.add(compensation);
    }
}
