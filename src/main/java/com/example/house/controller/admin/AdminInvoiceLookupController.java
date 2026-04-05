package com.example.house.controller.admin;

import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class AdminInvoiceLookupController {
    private final AdminService service;

    public AdminInvoiceLookupController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.InvoiceItem> invoices() {
        return service.invoices();
    }

    public void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        service.searchInvoices(roomNumber, month, year, paid);
    }

    public void reloadInvoices() {
        service.reloadInvoices();
    }
}

