package com.example.house.view.admin.data;

import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminRevenuePeriod;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AdminDataStore {
    record RateItem(RateType type, double unitPrice) {
    }

    record RoomItem(Integer id, String roomNumber, Integer floor, double size, double baseRent, String furniture,
                    RoomStatus status) {
    }

    record StaffAccountItem(Integer id, String username, String fullName, String shiftSchedule) {
    }

    record RevenueRow(String label, double invoiceTotal, double compensationTotal, double total) {
    }

    record CheckoutItem(Integer contractId, String roomNumber, String tenantName, double deposit, LocalDate startDate) {
    }

    record CheckoutSummary(double deposit, double unpaidInvoices, double unpaidCompensations, double refundAmount) {
    }

    record FeedbackItem(Integer id, String roomNumber, String content, FeedbackStatus status, LocalDateTime sentAt) {
    }

    record InvoiceItem(Integer id, String roomNumber, int month, int year, double total, boolean paid,
                       String paymentMethod) {
    }

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

    void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue);

    CheckoutSummary buildCheckoutSummary(Integer contractId);

    void addCompensation(Integer contractId, double amount, String reason);

    void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod);

    FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status);

    void getFeedbacksByStatus(FeedbackStatus status);

    void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid);

    void reloadInvoices();
}
