package com.example.house.service.staff;

import com.example.house.model.entity.Invoice;

import java.time.YearMonth;
import java.util.List;

public interface BillingService {
    List<Invoice> processAutoBilling(YearMonth period);
    List<Invoice> getInvoices();
}


