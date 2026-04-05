package com.example.house.repository.impl.admin.feature;

import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.RoomStatus;
import com.example.house.repository.admin.feature.AdminCheckoutRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminCheckoutRepositoryImpl implements AdminCheckoutRepository {
    private final AdminDataStore dataStore;

    public AdminCheckoutRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts() {
        return dataStore.pendingCheckouts();
    }

    @Override
    public AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId) {
        return dataStore.buildCheckoutSummary(contractId);
    }

    @Override
    public void addCompensation(Integer contractId, double amount, String reason) {
        dataStore.addCompensation(contractId, amount, reason);
    }

    @Override
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        dataStore.approveCheckout(contractId, roomStatus, paymentMethod);
    }
}
