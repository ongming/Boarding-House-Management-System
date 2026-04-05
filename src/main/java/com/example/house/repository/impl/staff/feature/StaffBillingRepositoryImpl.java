package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffBillingRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

import java.time.YearMonth;

public class StaffBillingRepositoryImpl implements StaffBillingRepository {
    private final StaffDataStore dataStore;

    public StaffBillingRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<StaffDataStore.InvoiceItem> invoices() {
        return dataStore.invoices();
    }

    @Override
    public int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee) {
        return dataStore.generateInvoices(month, electricRate, waterRate, garbageFee);
    }

    @Override
    public StaffDataStore.InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod) {
        return dataStore.markInvoicePaid(invoiceId, paymentMethod);
    }
}
