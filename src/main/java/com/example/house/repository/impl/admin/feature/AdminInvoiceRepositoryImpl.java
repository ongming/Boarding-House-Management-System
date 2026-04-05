package com.example.house.repository.impl.admin.feature;

import com.example.house.repository.admin.feature.AdminInvoiceRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminInvoiceRepositoryImpl implements AdminInvoiceRepository {
    private final AdminDataStore dataStore;

    public AdminInvoiceRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.InvoiceItem> invoices() {
        return dataStore.invoices();
    }

    @Override
    public void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        dataStore.searchInvoices(roomNumber, month, year, paid);
    }

    @Override
    public void reloadInvoices() {
        dataStore.reloadInvoices();
    }
}
