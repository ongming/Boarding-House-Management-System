package com.example.house.service.impl.admin;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import com.example.house.repository.impl.JpaAdminDataStore;
import javafx.collections.ObservableList;

public class AdminServiceImpl implements AdminService {
    private final AdminDataStore dataStore;

    public AdminServiceImpl() {
        this(new JpaAdminDataStore());
    }

    public AdminServiceImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.RateItem> rateConfigs() { return dataStore.rateConfigs(); }
    @Override
    public ObservableList<AdminDataStore.RoomItem> rooms() { return dataStore.rooms(); }
    @Override
    public ObservableList<AdminDataStore.StaffAccountItem> staffAccounts() { return dataStore.staffAccounts(); }
    @Override
    public ObservableList<AdminDataStore.RevenueRow> revenueRows() { return dataStore.revenueRows(); }
    @Override
    public ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts() { return dataStore.pendingCheckouts(); }
    @Override
    public ObservableList<AdminDataStore.FeedbackItem> feedbacks() { return dataStore.feedbacks(); }
    @Override
    public ObservableList<AdminDataStore.InvoiceItem> invoices() { return dataStore.invoices(); }

    @Override
    public void refreshAll() { dataStore.refreshAll(); }
    @Override
    public AdminDataStore.RateItem saveRate(RateType type, double unitPrice) { return dataStore.saveRate(type, unitPrice); }
    @Override
    public AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item) { return dataStore.saveRoom(item); }
    @Override
    public void deleteRoom(Integer roomId) { dataStore.deleteRoom(roomId); }
    @Override
    public AdminDataStore.StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule) {
        return dataStore.createStaffAccount(username, password, fullName, shiftSchedule);
    }
    @Override
    public void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue) {
        dataStore.refreshRevenue(period, year, periodValue);
    }
    @Override
    public AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId) {
        return dataStore.buildCheckoutSummary(contractId);
    }
    @Override
    public void addCompensation(Integer contractId, double amount, String reason) { dataStore.addCompensation(contractId, amount, reason); }
    @Override
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        dataStore.approveCheckout(contractId, roomStatus, paymentMethod);
    }
    @Override
    public AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status) { return dataStore.updateFeedbackStatus(id, status); }
    @Override
    public void getFeedbacksByStatus(FeedbackStatus status) { dataStore.getFeedbacksByStatus(status); }
    @Override
    public void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        dataStore.searchInvoices(roomNumber, month, year, paid);
    }
    @Override
    public void reloadInvoices() { dataStore.reloadInvoices(); }
}
