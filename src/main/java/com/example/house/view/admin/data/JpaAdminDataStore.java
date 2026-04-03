package com.example.house.view.admin.data;

import com.example.house.model.entity.Account;
import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Employee;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.RateConfig;
import com.example.house.model.entity.Room;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminCheckoutSummary;
import com.example.house.service.admin.AdminRevenuePeriod;
import com.example.house.service.admin.AdminRevenueReport;
import com.example.house.service.admin.AdminRevenueRow;
import com.example.house.service.admin.AdminWorkflowService;
import com.example.house.service.impl.AdminWorkflowServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class JpaAdminDataStore implements AdminDataStore {
    private final AdminWorkflowService adminService;

    private final ObservableList<RateItem> rateConfigs = FXCollections.observableArrayList();
    private final ObservableList<RoomItem> rooms = FXCollections.observableArrayList();
    private final ObservableList<StaffAccountItem> staffAccounts = FXCollections.observableArrayList();
    private final ObservableList<RevenueRow> revenueRows = FXCollections.observableArrayList();
    private final ObservableList<CheckoutItem> pendingCheckouts = FXCollections.observableArrayList();
    private final ObservableList<FeedbackItem> feedbacks = FXCollections.observableArrayList();
    private final ObservableList<InvoiceItem> invoices = FXCollections.observableArrayList();

    public JpaAdminDataStore() {
        this(new AdminWorkflowServiceImpl());
    }

    public JpaAdminDataStore(AdminWorkflowService adminService) {
        this.adminService = adminService;
        refreshAll();
    }

    @Override
    public ObservableList<RateItem> rateConfigs() {
        return rateConfigs;
    }

    @Override
    public ObservableList<RoomItem> rooms() {
        return rooms;
    }

    @Override
    public ObservableList<StaffAccountItem> staffAccounts() {
        return staffAccounts;
    }

    @Override
    public ObservableList<RevenueRow> revenueRows() {
        return revenueRows;
    }

    @Override
    public ObservableList<CheckoutItem> pendingCheckouts() {
        return pendingCheckouts;
    }

    @Override
    public ObservableList<FeedbackItem> feedbacks() {
        return feedbacks;
    }

    @Override
    public ObservableList<InvoiceItem> invoices() {
        return invoices;
    }

    @Override
    public void refreshAll() {
        rateConfigs.setAll(adminService.getRateConfigs().stream().map(this::toRateItem).toList());
        rooms.setAll(adminService.getRooms().stream().map(this::toRoomItem).toList());
        staffAccounts.setAll(adminService.getEmployees().stream().map(this::toStaffItem).toList());
        pendingCheckouts.setAll(adminService.getActiveContracts().stream().map(this::toCheckoutItem).toList());
        feedbacks.setAll(adminService.getFeedbacks().stream().map(this::toFeedbackItem).toList());
        invoices.setAll(adminService.findInvoices(null, null, null, null).stream().map(this::toInvoiceItem).toList());
    }

    @Override
    public RateItem saveRate(RateType type, double unitPrice) {
        RateConfig saved = adminService.saveRateConfig(type, BigDecimal.valueOf(unitPrice));
        RateItem item = toRateItem(saved);
        upsertRate(item);
        return item;
    }

    @Override
    public RoomItem saveRoom(RoomItem item) {
        Room room = toRoomEntity(item);
        Room saved = adminService.saveRoom(room);
        RoomItem savedItem = toRoomItem(saved);
        upsertRoom(savedItem);
        return savedItem;
    }

    @Override
    public void deleteRoom(Integer roomId) {
        adminService.deleteRoom(roomId);
        rooms.removeIf(room -> room.id() != null && room.id().equals(roomId));
    }

    @Override
    public StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule) {
        Account account = adminService.createStaffAccount(username, password, fullName, shiftSchedule);
        Employee employee = account.getEmployee();
        StaffAccountItem item = new StaffAccountItem(
                employee == null ? null : employee.getId(),
                account.getUsername(),
                account.getFullName(),
                employee == null ? "" : safe(employee.getShiftSchedule())
        );
        staffAccounts.add(item);
        return item;
    }

    @Override
    public void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue) {
        AdminRevenueReport report = adminService.getRevenueReport(period, year);
        List<AdminRevenueRow> rows = report.rows();
        if (periodValue != null) {
            rows = rows.stream().filter(row -> matchPeriod(row.label(), periodValue)).toList();
        }
        revenueRows.setAll(rows.stream().map(this::toRevenueRow).toList());
    }

    @Override
    public CheckoutSummary buildCheckoutSummary(Integer contractId) {
        AdminCheckoutSummary summary = adminService.buildCheckoutSummary(contractId);
        return new CheckoutSummary(
                value(summary.deposit()),
                value(summary.unpaidInvoices()),
                value(summary.unpaidCompensations()),
                value(summary.refundAmount())
        );
    }

    @Override
    public void addCompensation(Integer contractId, double amount, String reason) {
        adminService.addCompensation(contractId, BigDecimal.valueOf(amount), reason);
    }

    @Override
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        adminService.approveCheckout(contractId, roomStatus, paymentMethod);
        refreshAll();
    }

    @Override
    public FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status) {
        Feedback feedback = adminService.updateFeedbackStatus(id, status);
        FeedbackItem item = toFeedbackItem(feedback);
        upsertFeedback(item);
        return item;
    }

    @Override
    public void searchInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        invoices.setAll(adminService.findInvoices(roomNumber, month, year, paid).stream().map(this::toInvoiceItem).toList());
    }

    @Override
    public void reloadInvoices() {
        searchInvoices(null, null, null, null);
    }

    @Override
    public void getFeedbacksByStatus(FeedbackStatus status) {
        if (status == null) {
            feedbacks.setAll(adminService.getFeedbacks().stream().map(this::toFeedbackItem).toList());
            return;
        }
        feedbacks.setAll(adminService.getFeedbacksByStatus(status).stream().map(this::toFeedbackItem).toList());
    }

    private RateItem toRateItem(RateConfig config) {
        RateType type = config.getType() == null ? RateType.DIEN : config.getType();
        double unitPrice = value(config.getUnitPrice());
        return new RateItem(type, unitPrice);
    }

    private void upsertRate(RateItem item) {
        for (int i = 0; i < rateConfigs.size(); i++) {
            if (rateConfigs.get(i).type() == item.type()) {
                rateConfigs.set(i, item);
                return;
            }
        }
        rateConfigs.add(item);
    }

    private RoomItem toRoomItem(Room room) {
        return new RoomItem(
                room.getId(),
                room.getRoomNumber(),
                room.getFloor(),
                value(room.getSize()),
                value(room.getBaseRent()),
                safe(room.getFurnitureList()),
                room.getStatus() == null ? RoomStatus.TRONG : room.getStatus()
        );
    }

    private Room toRoomEntity(RoomItem item) {
        Room room = new Room();
        room.setRoomNumber(item.roomNumber());
        room.setFloor(item.floor());
        room.setSize(BigDecimal.valueOf(item.size()));
        room.setBaseRent(BigDecimal.valueOf(item.baseRent()));
        room.setFurnitureList(item.furniture());
        room.setStatus(item.status());
        if (item.id() != null) {
            room.setId(item.id());
        }
        return room;
    }

    private void upsertRoom(RoomItem item) {
        for (int i = 0; i < rooms.size(); i++) {
            if (Objects.equals(rooms.get(i).id(), item.id())) {
                rooms.set(i, item);
                return;
            }
        }
        rooms.add(item);
    }

    private void upsertFeedback(FeedbackItem item) {
        for (int i = 0; i < feedbacks.size(); i++) {
            if (Objects.equals(feedbacks.get(i).id(), item.id())) {
                feedbacks.set(i, item);
                return;
            }
        }
        feedbacks.add(item);
    }

    private StaffAccountItem toStaffItem(Employee employee) {
        Account account = employee.getAccount();
        return new StaffAccountItem(
                employee.getId(),
                account == null ? "" : account.getUsername(),
                account == null ? "" : safe(account.getFullName()),
                safe(employee.getShiftSchedule())
        );
    }

    private CheckoutItem toCheckoutItem(Contract contract) {
        String roomNumber = contract.getRoom() == null ? "" : contract.getRoom().getRoomNumber();
        String tenantName = contract.getTenant() == null ? "" : contract.getTenant().getFullName();
        double deposit = value(contract.getDeposit());
        LocalDate startDate = contract.getStartDate();
        return new CheckoutItem(contract.getId(), roomNumber, tenantName, deposit, startDate);
    }

    private FeedbackItem toFeedbackItem(Feedback feedback) {
        String roomNumber = feedback.getRoom() == null ? "" : feedback.getRoom().getRoomNumber();
        FeedbackStatus status = feedback.getStatus() == null ? FeedbackStatus.CHO_XU_LY : feedback.getStatus();
        LocalDateTime sentAt = feedback.getSentAt() == null ? LocalDateTime.now() : feedback.getSentAt();
        return new FeedbackItem(feedback.getId(), roomNumber, safe(feedback.getContent()), status, sentAt);
    }

    private InvoiceItem toInvoiceItem(Invoice invoice) {
        String roomNumber = invoice.getContract() != null && invoice.getContract().getRoom() != null
                ? invoice.getContract().getRoom().getRoomNumber() : "";
        int month = invoice.getMonth() == null ? 0 : invoice.getMonth();
        int year = invoice.getYear() == null ? 0 : invoice.getYear();
        double total = value(invoice.getTotal());
        boolean paid = Boolean.TRUE.equals(invoice.getPaid());
        String method = invoice.getPaymentMethod() == null ? "CHUA_THU" : invoice.getPaymentMethod().name();
        return new InvoiceItem(invoice.getId(), roomNumber, month, year, total, paid, method);
    }

    private RevenueRow toRevenueRow(AdminRevenueRow row) {
        return new RevenueRow(
                row.label(),
                value(row.invoiceTotal()),
                value(row.compensationTotal()),
                value(row.total())
        );
    }

    private boolean matchPeriod(String label, int value) {
        if (label == null) {
            return false;
        }
        String digits = label.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return false;
        }
        return Integer.parseInt(digits) == value;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private double value(BigDecimal number) {
        return number == null ? 0.0 : number.doubleValue();
    }
}
