package com.example.house.repository.impl;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.UtilityReading;
import com.example.house.model.entity.Vehicle;
import com.example.house.model.dto.staff.ContractCreationRequest;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.InvoicePaymentMethod;
import com.example.house.service.impl.staff.StaffDomainServiceImpl;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffDomainService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class JpaStaffDataStore implements StaffDataStore {
    private final StaffDomainService workflowService;

    private final ObservableList<String> roomCodes = FXCollections.observableArrayList();
    private final ObservableList<ContractItem> contracts = FXCollections.observableArrayList();
    private final ObservableList<VehicleItem> vehicles = FXCollections.observableArrayList();
    private final ObservableList<OccupancyItem> occupancies = FXCollections.observableArrayList();
    private final ObservableList<MeterReadingItem> meterReadings = FXCollections.observableArrayList();
    private final ObservableList<InvoiceItem> invoices = FXCollections.observableArrayList();
    private final ObservableList<FeedbackItem> feedbacks = FXCollections.observableArrayList();

    public JpaStaffDataStore() {
        this(new StaffDomainServiceImpl());
    }

    public JpaStaffDataStore(StaffDomainService workflowService) {
        this.workflowService = workflowService;
        refreshAll();
    }

    @Override
    public ObservableList<String> roomCodes() {
        return roomCodes;
    }

    @Override
    public ObservableList<ContractItem> contracts() {
        return contracts;
    }

    @Override
    public ObservableList<VehicleItem> vehicles() {
        return vehicles;
    }

    @Override
    public ObservableList<OccupancyItem> occupancies() {
        return occupancies;
    }

    @Override
    public ObservableList<MeterReadingItem> meterReadings() {
        return meterReadings;
    }

    @Override
    public ObservableList<InvoiceItem> invoices() {
        return invoices;
    }

    @Override
    public ObservableList<FeedbackItem> feedbacks() {
        return feedbacks;
    }

    @Override
    public ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit) {
        Contract contract = workflowService.processContractCreation(new ContractCreationRequest(
                roomCode,
                tenantName,
                "AUTO-" + System.currentTimeMillis(),
                "",
                BigDecimal.valueOf(deposit),
                BigDecimal.valueOf(rentFee),
                LocalDate.now(),
                LocalDate.now(),
                null,
                null,
                1,
                null,
                null
        ));

        refreshAll();
        return toContractItem(contract);
    }

    @Override
    public ContractItem addContractFull(String roomCode, String tenantName, String tenantCccd, String tenantPhone,
                                        LocalDate startDate, LocalDate moveInDate, LocalDate endDate,
                                        String contractImageUrl, int occupantCount, double rentFee, double deposit) {
        Contract contract = workflowService.processContractCreation(new ContractCreationRequest(
                roomCode,
                tenantName,
                tenantCccd,
                tenantPhone,
                BigDecimal.valueOf(deposit),
                BigDecimal.valueOf(rentFee),
                startDate,
                moveInDate,
                endDate,
                contractImageUrl,
                occupantCount,
                null,
                null
        ));

        refreshAll();
        return toContractItem(contract);
    }

    @Override
    public ContractItem updateContractMoveInDate(int contractId, LocalDate moveInDate) {
        Contract contract = workflowService.updateContractMoveInDate(contractId, moveInDate);
        refreshAll();
        return toContractItem(contract);
    }

    @Override
    public VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
        Vehicle vehicle = workflowService.registerVehicle(roomCode, vehicleType, plateNumber);
        refreshAll();

        String mappedRoom = vehicle.getContract() != null && vehicle.getContract().getRoom() != null
                ? vehicle.getContract().getRoom().getRoomNumber()
                : roomCode;

        return new VehicleItem(vehicle.getId(), mappedRoom, vehicle.getVehicleType(), vehicle.getLicensePlate(), monthlyFee);
    }

    @Override
    public OccupancyItem upsertOccupancy(String roomCode, int peopleCount) {
        workflowService.updateOccupancy(roomCode, peopleCount);

        refreshAll();
        return occupancies.stream()
                .filter(item -> item.roomCode().equalsIgnoreCase(roomCode))
                .findFirst()
                .orElse(new OccupancyItem(roomCode, peopleCount, LocalDateTime.now()));
    }

    @Override
    public MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric,
                                            int oldWater, int newWater) {
        UtilityReading reading = workflowService.saveUtilityReading(roomCode, month, oldElectric, newElectric, oldWater, newWater);
        refreshAll();

        return new MeterReadingItem(
                reading.getId(),
                reading.getRoom().getRoomNumber(),
                YearMonth.of(reading.getYear(), reading.getMonth()),
                reading.getOldElectricNumber(),
                reading.getNewElectricNumber(),
                reading.getOldWaterNumber(),
                reading.getNewWaterNumber()
        );
    }

    @Override
    public int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee) {
        int before = invoices.size();
        workflowService.processAutoBilling(month);
        refreshAll();
        return Math.max(0, invoices.size() - before);
    }

    @Override
    public InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod) {
        InvoicePaymentMethod method = "TIEN_MAT".equalsIgnoreCase(paymentMethod) || "Tiá»n máº·t".equalsIgnoreCase(paymentMethod)
                ? InvoicePaymentMethod.TIEN_MAT
                : InvoicePaymentMethod.CHUYEN_KHOAN;

        Invoice invoice = workflowService.processPayment(invoiceId, method);
        refreshAll();
        return toInvoiceItem(invoice);
    }

    @Override
    public FeedbackItem addFeedback(String roomCode, String title, String description, String priority) {
        Integer roomId = workflowService.getRooms().stream()
                .filter(room -> roomCode.equalsIgnoreCase(room.getRoomNumber()))
                .map(room -> room.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Feedback feedback = workflowService.saveIncident(roomId, title + ": " + description, 1);
        refreshAll();

        return new FeedbackItem(
                feedback.getId(),
                feedback.getRoom() != null ? feedback.getRoom().getRoomNumber() : roomCode,
                title,
                description,
                priority,
                feedback.getStatus() != null ? feedback.getStatus().name() : FeedbackStatus.CHO_XU_LY.name(),
                feedback.getSentAt()
        );
    }

    private void refreshAll() {
        roomCodes.setAll(workflowService.getRooms().stream()
                .map(room -> room.getRoomNumber())
                .filter(code -> code != null && !code.isBlank())
                .sorted(String::compareToIgnoreCase)
                .toList());

        contracts.setAll(workflowService.getContracts().stream().map(this::toContractItem).toList());
        vehicles.setAll(workflowService.getVehicles().stream().map(this::toVehicleItem).toList());
        meterReadings.setAll(workflowService.getUtilityReadings().stream().map(this::toReadingItem).toList());
        invoices.setAll(workflowService.getInvoices().stream().map(this::toInvoiceItem).toList());
        feedbacks.setAll(workflowService.getFeedbacks().stream().map(this::toFeedbackItem).toList());

        occupancies.setAll(workflowService.getContracts().stream()
                .filter(contract -> contract.getRoom() != null)
                .map(contract -> new OccupancyItem(
                        contract.getRoom().getRoomNumber(),
                        contract.getOccupantCount() == null ? 0 : contract.getOccupantCount(),
                        LocalDateTime.now()))
                .toList());
    }

    private ContractItem toContractItem(Contract contract) {
        String roomCode = contract.getRoom() != null ? contract.getRoom().getRoomNumber() : "";
        String tenantName = contract.getTenant() != null ? contract.getTenant().getFullName() : "";
        double roomFee = contract.getRoom() != null && contract.getRoom().getBaseRent() != null
                ? contract.getRoom().getBaseRent().doubleValue() : 0.0;
        double deposit = contract.getDeposit() != null ? contract.getDeposit().doubleValue() : 0.0;
        LocalDate startDate = contract.getStartDate() != null ? contract.getStartDate() : LocalDate.now();
        LocalDate moveInDate = contract.getMoveInDate();
        LocalDate endDate = contract.getEndDate();
        String contractImageUrl = contract.getContractImageUrl();

        return new ContractItem(
                contract.getId(),
                roomCode,
                tenantName,
                roomFee,
                deposit,
                startDate,
                moveInDate,
                endDate,
                contractImageUrl,
                LocalDateTime.now()
        );
    }

    private VehicleItem toVehicleItem(Vehicle vehicle) {
        String roomCode = vehicle.getContract() != null && vehicle.getContract().getRoom() != null
                ? vehicle.getContract().getRoom().getRoomNumber() : "";
        return new VehicleItem(vehicle.getId(), roomCode, vehicle.getVehicleType(), vehicle.getLicensePlate(), 0.0);
    }

    private MeterReadingItem toReadingItem(UtilityReading reading) {
        return new MeterReadingItem(
                reading.getId(),
                reading.getRoom() != null ? reading.getRoom().getRoomNumber() : "",
                YearMonth.of(reading.getYear(), reading.getMonth()),
                reading.getOldElectricNumber(),
                reading.getNewElectricNumber(),
                reading.getOldWaterNumber(),
                reading.getNewWaterNumber()
        );
    }

    private InvoiceItem toInvoiceItem(Invoice invoice) {
        String roomCode = invoice.getContract() != null && invoice.getContract().getRoom() != null
                ? invoice.getContract().getRoom().getRoomNumber() : "";

        return new InvoiceItem(
                invoice.getId(),
                roomCode,
                YearMonth.of(invoice.getYear(), invoice.getMonth()),
                value(invoice.getRoomFee()),
                value(invoice.getElectricFee()),
                value(invoice.getWaterFee()),
                value(invoice.getParkingFee()),
                value(invoice.getGarbageFee()),
                value(invoice.getTotal()),
                Boolean.TRUE.equals(invoice.getPaid()),
                invoice.getPaymentMethod() == null ? "CHUA_THU" : invoice.getPaymentMethod().name(),
                invoice.getCreatedAt() == null ? LocalDateTime.now() : invoice.getCreatedAt()
        );
    }

    private FeedbackItem toFeedbackItem(Feedback feedback) {
        return new FeedbackItem(
                feedback.getId(),
                feedback.getRoom() != null ? feedback.getRoom().getRoomNumber() : "",
                feedback.getContent(),
                feedback.getContent(),
                "TRUNG_BINH",
                feedback.getStatus() == null ? FeedbackStatus.CHO_XU_LY.name() : feedback.getStatus().name(),
                feedback.getSentAt() == null ? LocalDateTime.now() : feedback.getSentAt()
        );
    }

    private double value(BigDecimal number) {
        return number == null ? 0.0 : number.doubleValue();
    }
}
