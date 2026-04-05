package com.example.house.repository.staff.feature;

import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

import java.time.YearMonth;

public interface StaffBillingRepository {
    ObservableList<StaffDataStore.InvoiceItem> invoices();
    int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee);
    StaffDataStore.InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod);
}
