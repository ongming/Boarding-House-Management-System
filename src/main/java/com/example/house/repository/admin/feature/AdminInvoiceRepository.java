package com.example.house.repository.admin.feature;

import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminInvoiceRepository {
    ObservableList<AdminDataStore.InvoiceItem> invoices();
    void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid);
    void reloadInvoices();
}

