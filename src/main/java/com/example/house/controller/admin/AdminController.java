package com.example.house.controller.admin;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class AdminController {
    private final AdminService service;
    private final FeedbackManagementController feedbackController;
    private final RateConfigController rateConfigController;
    private final StaffAccountController staffAccountController;
    private final RevenueStatsController revenueStatsController;
    private final CheckoutApprovalController checkoutApprovalController;
    private final AdminInvoiceLookupController invoiceLookupController;

    public AdminController(AdminService service) {
        this.service = service;
        this.feedbackController = new FeedbackManagementController(service);
        this.rateConfigController = new RateConfigController(service);
        this.staffAccountController = new StaffAccountController(service);
        this.revenueStatsController = new RevenueStatsController(service);
        this.checkoutApprovalController = new CheckoutApprovalController(service);
        this.invoiceLookupController = new AdminInvoiceLookupController(service);
    }

    public ObservableList<AdminDataStore.RateItem> rateConfigs() { return rateConfigController.rateConfigs(); }
    public ObservableList<AdminDataStore.RoomItem> rooms() { return service.rooms(); }
    public ObservableList<AdminDataStore.StaffAccountItem> staffAccounts() { return staffAccountController.staffAccounts(); }
    public ObservableList<AdminDataStore.RevenueRow> revenueRows() { return revenueStatsController.revenueRows(); }
    public ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts() { return checkoutApprovalController.pendingCheckouts(); }
    public ObservableList<AdminDataStore.FeedbackItem> feedbacks() { return feedbackController.feedbacks(); }
    public ObservableList<AdminDataStore.InvoiceItem> invoices() { return invoiceLookupController.invoices(); }

    public void refreshAll() { feedbackController.refreshAll(); }
    public AdminDataStore.RateItem saveRate(RateType type, double unitPrice) { return rateConfigController.saveRate(type, unitPrice); }
    public AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item) { return service.saveRoom(item); }
    public void deleteRoom(Integer roomId) { service.deleteRoom(roomId); }
    public AdminDataStore.StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule) {
        return staffAccountController.createStaffAccount(username, password, fullName, shiftSchedule);
    }
    public AdminDataStore.StaffAccountItem updateStaffAccount(Integer employeeId, String username, String password, String fullName, String shiftSchedule) {
        return staffAccountController.updateStaffAccount(employeeId, username, password, fullName, shiftSchedule);
    }
    public void deleteStaffAccount(Integer employeeId) {
        staffAccountController.deleteStaffAccount(employeeId);
    }
    public void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue) {
        revenueStatsController.refreshRevenue(period, year, periodValue);
    }
    public AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId) { return checkoutApprovalController.buildCheckoutSummary(contractId); }
    public void addCompensation(Integer contractId, double amount, String reason) { checkoutApprovalController.addCompensation(contractId, amount, reason); }
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        checkoutApprovalController.approveCheckout(contractId, roomStatus, paymentMethod);
    }
    public AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status) {
        return feedbackController.updateFeedbackStatus(id, status);
    }
    public void getFeedbacksByStatus(FeedbackStatus status) { feedbackController.getFeedbacksByStatus(status); }
    public void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid) { invoiceLookupController.searchInvoices(roomNumber, month, year, paid); }
    public void reloadInvoices() { invoiceLookupController.reloadInvoices(); }
}
