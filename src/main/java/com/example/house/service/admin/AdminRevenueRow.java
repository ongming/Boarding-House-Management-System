package com.example.house.service.admin;

import java.math.BigDecimal;

public record AdminRevenueRow(String label, BigDecimal invoiceTotal, BigDecimal compensationTotal) {
    public BigDecimal total() {
        BigDecimal invoice = invoiceTotal == null ? BigDecimal.ZERO : invoiceTotal;
        BigDecimal compensation = compensationTotal == null ? BigDecimal.ZERO : compensationTotal;
        return invoice.add(compensation);
    }
}
