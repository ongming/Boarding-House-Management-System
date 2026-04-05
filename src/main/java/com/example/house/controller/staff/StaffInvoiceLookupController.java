package com.example.house.controller.staff;

import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

public class StaffInvoiceLookupController {
    private final StaffService service;

    public StaffInvoiceLookupController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.InvoiceItem> invoices() {
        return service.invoices();
    }
}

