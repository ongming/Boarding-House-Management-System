package com.example.house.service.admin;

import java.math.BigDecimal;

public record AdminCheckoutSummary(BigDecimal deposit, BigDecimal unpaidInvoices, BigDecimal unpaidCompensations, BigDecimal refundAmount) {
}
