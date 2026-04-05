package com.example.house.service.admin;

import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import javafx.collections.ObservableList;

public interface AdminDataStore extends com.example.house.model.dto.admin.AdminDataStore {
    ObservableList<RateItem> rateConfigs();
    ObservableList<RoomItem> rooms();
    ObservableList<StaffAccountItem> staffAccounts();
    ObservableList<RevenueRow> revenueRows();
    ObservableList<CheckoutItem> pendingCheckouts();
    ObservableList<FeedbackItem> feedbacks();
    ObservableList<InvoiceItem> invoices();

    void refreshAll();
    RateItem saveRate(RateType type, double unitPrice);
    RoomItem saveRoom(RoomItem item);
    void deleteRoom(Integer roomId);
    StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule);
    void refreshRevenue(com.example.house.model.enums.AdminRevenuePeriod period, int year, Integer periodValue);
    CheckoutSummary buildCheckoutSummary(Integer contractId);
    void addCompensation(Integer contractId, double amount, String reason);
    void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod);
    FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status);
    void getFeedbacksByStatus(FeedbackStatus status);
    void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid);
    void reloadInvoices();
}
