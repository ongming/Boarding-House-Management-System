package com.example.house.service.admin;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import javafx.collections.ObservableList;

public interface AdminService {
    ObservableList<AdminDataStore.RateItem> rateConfigs();
    ObservableList<AdminDataStore.RoomItem> rooms();
    ObservableList<AdminDataStore.StaffAccountItem> staffAccounts();
    ObservableList<AdminDataStore.RevenueRow> revenueRows();
    ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts();
    ObservableList<AdminDataStore.FeedbackItem> feedbacks();
    ObservableList<AdminDataStore.InvoiceItem> invoices();

    void refreshAll();
    AdminDataStore.RateItem saveRate(RateType type, double unitPrice);
    AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item);
    void deleteRoom(Integer roomId);
    AdminDataStore.StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule);
    AdminDataStore.StaffAccountItem updateStaffAccount(Integer employeeId, String username, String password, String fullName, String shiftSchedule);
    void deleteStaffAccount(Integer employeeId);
    void refreshRevenue(com.example.house.model.enums.AdminRevenuePeriod period, int year, Integer periodValue);
    AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId);
    void addCompensation(Integer contractId, double amount, String reason);
    void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod);
    AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status);
    void getFeedbacksByStatus(FeedbackStatus status);
    void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid);
    void reloadInvoices();
}

