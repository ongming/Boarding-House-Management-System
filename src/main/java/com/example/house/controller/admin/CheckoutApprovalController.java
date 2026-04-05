package com.example.house.controller.admin;

import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class CheckoutApprovalController {
    private final AdminService service;

    public CheckoutApprovalController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts() {
        return service.pendingCheckouts();
    }

    public AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId) {
        return service.buildCheckoutSummary(contractId);
    }

    public void addCompensation(Integer contractId, double amount, String reason) {
        service.addCompensation(contractId, amount, reason);
    }

    public void approveCheckout(Integer contractId,
                                RoomStatus roomStatus,
                                CompensationPaymentMethod paymentMethod) {
        service.approveCheckout(contractId, roomStatus, paymentMethod);
    }
}

