package com.example.house.controller.staff;

import com.example.house.controller.shared.ControllerInputParser;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

public class PaymentController {
    private final StaffService service;

    public PaymentController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.InvoiceItem> invoices() {
        return service.invoices();
    }

    public void markInvoicePaid(StaffDataStore.InvoiceItem invoice, String paymentMethod) {
        if (invoice == null) {
            throw new IllegalArgumentException("Chon hoa don can thanh toan");
        }
        service.markInvoicePaid(
                invoice.id(),
                ControllerInputParser.required(paymentMethod, "Phuong thuc thanh toan")
        );
    }
}

