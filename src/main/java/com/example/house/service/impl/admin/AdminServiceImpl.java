package com.example.house.service.impl.admin;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.repository.admin.feature.AdminCheckoutRepository;
import com.example.house.repository.admin.feature.AdminFeedbackRepository;
import com.example.house.repository.admin.feature.AdminInvoiceRepository;
import com.example.house.repository.admin.feature.AdminRateConfigRepository;
import com.example.house.repository.admin.feature.AdminRevenueRepository;
import com.example.house.repository.admin.feature.AdminRoomRepository;
import com.example.house.repository.admin.feature.AdminStaffAccountRepository;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class AdminServiceImpl implements AdminService {
    private final AdminRateConfigRepository rateConfigRepository;
    private final AdminRoomRepository roomRepository;
    private final AdminStaffAccountRepository staffAccountRepository;
    private final AdminRevenueRepository revenueRepository;
    private final AdminCheckoutRepository checkoutRepository;
    private final AdminFeedbackRepository feedbackRepository;
    private final AdminInvoiceRepository invoiceRepository;

    public AdminServiceImpl(AdminRateConfigRepository rateConfigRepository,
                            AdminRoomRepository roomRepository,
                            AdminStaffAccountRepository staffAccountRepository,
                            AdminRevenueRepository revenueRepository,
                            AdminCheckoutRepository checkoutRepository,
                            AdminFeedbackRepository feedbackRepository,
                            AdminInvoiceRepository invoiceRepository) {
        this.rateConfigRepository = rateConfigRepository;
        this.roomRepository = roomRepository;
        this.staffAccountRepository = staffAccountRepository;
        this.revenueRepository = revenueRepository;
        this.checkoutRepository = checkoutRepository;
        this.feedbackRepository = feedbackRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public ObservableList<AdminDataStore.RateItem> rateConfigs() { return rateConfigRepository.rateConfigs(); }
    @Override
    public ObservableList<AdminDataStore.RoomItem> rooms() { return roomRepository.rooms(); }
    @Override
    public ObservableList<AdminDataStore.StaffAccountItem> staffAccounts() { return staffAccountRepository.staffAccounts(); }
    @Override
    public ObservableList<AdminDataStore.RevenueRow> revenueRows() { return revenueRepository.revenueRows(); }
    @Override
    public ObservableList<AdminDataStore.CheckoutItem> pendingCheckouts() { return checkoutRepository.pendingCheckouts(); }
    @Override
    public ObservableList<AdminDataStore.FeedbackItem> feedbacks() { return feedbackRepository.feedbacks(); }
    @Override
    public ObservableList<AdminDataStore.InvoiceItem> invoices() { return invoiceRepository.invoices(); }

    @Override
    public void refreshAll() { feedbackRepository.refreshAll(); }
    @Override
    public AdminDataStore.RateItem saveRate(RateType type, double unitPrice) { return rateConfigRepository.saveRate(type, unitPrice); }
    @Override
    public AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item) { return roomRepository.saveRoom(item); }
    @Override
    public void deleteRoom(Integer roomId) { roomRepository.deleteRoom(roomId); }
    @Override
    public AdminDataStore.StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule) {
        return staffAccountRepository.createStaffAccount(username, password, fullName, shiftSchedule);
    }
    @Override
    public void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue) {
        revenueRepository.refreshRevenue(period, year, periodValue);
    }
    @Override
    public AdminDataStore.CheckoutSummary buildCheckoutSummary(Integer contractId) {
        return checkoutRepository.buildCheckoutSummary(contractId);
    }
    @Override
    public void addCompensation(Integer contractId, double amount, String reason) { checkoutRepository.addCompensation(contractId, amount, reason); }
    @Override
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        checkoutRepository.approveCheckout(contractId, roomStatus, paymentMethod);
    }
    @Override
    public AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status) { return feedbackRepository.updateFeedbackStatus(id, status); }
    @Override
    public void getFeedbacksByStatus(FeedbackStatus status) { feedbackRepository.getFeedbacksByStatus(status); }
    @Override
    public void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        invoiceRepository.searchInvoices(roomNumber, month, year, paid);
    }
    @Override
    public void reloadInvoices() { invoiceRepository.reloadInvoices(); }
}
