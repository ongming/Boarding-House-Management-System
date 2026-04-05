package com.example.house.repository.admin.feature;

import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminCheckoutRepository {
    ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts();
    AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId);
    void addCompensation(Integer contractId, double amount, String reason);
    void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod);
}

