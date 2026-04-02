package com.example.house.service.impl;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Employee;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.RateConfig;
import com.example.house.model.entity.Room;
import com.example.house.model.entity.Tenant;
import com.example.house.model.entity.UtilityReading;
import com.example.house.model.entity.Vehicle;
import com.example.house.model.enums.ContractStatus;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.InvoicePaymentMethod;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.repository.impl.JpaContractRepository;
import com.example.house.repository.impl.JpaEmployeeRepository;
import com.example.house.repository.impl.JpaFeedbackRepository;
import com.example.house.repository.impl.JpaInvoiceRepository;
import com.example.house.repository.impl.JpaRateConfigRepository;
import com.example.house.repository.impl.JpaRoomRepository;
import com.example.house.repository.impl.JpaTenantRepository;
import com.example.house.repository.impl.JpaUtilityReadingRepository;
import com.example.house.repository.impl.JpaVehicleRepository;
import com.example.house.repository.staff.ContractRepository;
import com.example.house.repository.staff.EmployeeRepository;
import com.example.house.repository.staff.FeedbackRepository;
import com.example.house.repository.staff.InvoiceRepository;
import com.example.house.repository.staff.RateConfigRepository;
import com.example.house.repository.staff.RoomRepository;
import com.example.house.repository.staff.TenantRepository;
import com.example.house.repository.staff.UtilityReadingRepository;
import com.example.house.repository.staff.VehicleRepository;
import com.example.house.service.staff.ContractCreationRequest;
import com.example.house.service.staff.StaffWorkflowService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class StaffWorkflowServiceImpl implements StaffWorkflowService {
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final UtilityReadingRepository utilityReadingRepository;
    private final InvoiceRepository invoiceRepository;
    private final FeedbackRepository feedbackRepository;
    private final RateConfigRepository rateConfigRepository;
    private final EmployeeRepository employeeRepository;

    public StaffWorkflowServiceImpl() {
        this(
                new JpaRoomRepository(),
                new JpaTenantRepository(),
                new JpaContractRepository(),
                new JpaVehicleRepository(),
                new JpaUtilityReadingRepository(),
                new JpaInvoiceRepository(),
                new JpaFeedbackRepository(),
                new JpaRateConfigRepository(),
                new JpaEmployeeRepository()
        );
    }

    public StaffWorkflowServiceImpl(RoomRepository roomRepository,
                                    TenantRepository tenantRepository,
                                    ContractRepository contractRepository,
                                    VehicleRepository vehicleRepository,
                                    UtilityReadingRepository utilityReadingRepository,
                                    InvoiceRepository invoiceRepository,
                                    FeedbackRepository feedbackRepository,
                                    RateConfigRepository rateConfigRepository,
                                    EmployeeRepository employeeRepository) {
        this.roomRepository = roomRepository;
        this.tenantRepository = tenantRepository;
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.utilityReadingRepository = utilityReadingRepository;
        this.invoiceRepository = invoiceRepository;
        this.feedbackRepository = feedbackRepository;
        this.rateConfigRepository = rateConfigRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean isRoomAvailable(Integer roomId) {
        return roomRepository.findById(roomId)
                .map(Room::getStatus)
                .map(status -> status == RoomStatus.TRONG)
                .orElse(false);
    }

    @Override
    public Contract processContractCreation(ContractCreationRequest request) {
        Room room = roomRepository.findByRoomNumber(request.roomNumber())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getStatus() != RoomStatus.TRONG) {
            throw new IllegalArgumentException("Room is not available");
        }

        Tenant tenant = new Tenant();
        tenant.setFullName(request.tenantName());
        tenant.setCccdNumber(request.tenantCccd());
        tenant.setPhoneNumber(request.tenantPhone());
        tenant = tenantRepository.save(tenant);

        Contract contract = new Contract();
        contract.setRoom(room);
        contract.setTenant(tenant);
        contract.setStartDate(request.startDate());
        contract.setDeposit(defaultBigDecimal(request.deposit()));
        contract.setOccupantCount(request.occupantCount() == null ? 1 : request.occupantCount());
        contract.setStatus(ContractStatus.HIEU_LUC);
        contract = contractRepository.save(contract);

        if (request.vehicleType() != null && !request.vehicleType().isBlank()
                && request.vehiclePlate() != null && !request.vehiclePlate().isBlank()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setContract(contract);
            vehicle.setVehicleType(request.vehicleType());
            vehicle.setLicensePlate(request.vehiclePlate());
            vehicleRepository.save(vehicle);
        }

        if (request.roomFee() != null) {
            room.setBaseRent(request.roomFee());
        }
        room.setStatus(RoomStatus.DA_THUE);
        roomRepository.save(room);
        return contract;
    }

    @Override
    public UtilityReading saveUtilityReading(String roomNumber, YearMonth period, int oldElectric, int newElectric,
                                             int oldWater, int newWater) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        UtilityReading reading = new UtilityReading();
        reading.setRoom(room);
        reading.setMonth(period.getMonthValue());
        reading.setYear(period.getYear());
        reading.setOldElectricNumber(oldElectric);
        reading.setNewElectricNumber(newElectric);
        reading.setOldWaterNumber(oldWater);
        reading.setNewWaterNumber(newWater);
        reading.setRecordedAt(LocalDateTime.now());
        return utilityReadingRepository.save(reading);
    }

    @Override
    public List<Invoice> processAutoBilling(YearMonth period) {
        BigDecimal electricRate = getRate(RateType.DIEN);
        BigDecimal waterRate = getRate(RateType.NUOC);
        BigDecimal garbageRate = getRate(RateType.RAC);
        BigDecimal vehicleRate = getRate(RateType.XE_MAY);
        Optional<Employee> staffOptional = employeeRepository.findFirst();

        return contractRepository.findActiveContracts().stream()
                .filter(contract -> !invoiceRepository.existsByContractAndPeriod(
                        contract.getId(), period.getMonthValue(), period.getYear()))
                .map(contract -> buildInvoice(contract, period, electricRate, waterRate, garbageRate, vehicleRate,
                        staffOptional.orElse(null)))
                .map(invoiceRepository::save)
                .toList();
    }

    @Override
    public Invoice processPayment(Integer billId, InvoicePaymentMethod paymentType) {
        Invoice invoice = invoiceRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        invoice.setPaid(Boolean.TRUE);
        invoice.setPaymentMethod(paymentType);
        invoice.setPaidAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Feedback saveIncident(Integer roomId, String content, Integer currentStaffId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Employee employee = employeeRepository.findById(currentStaffId)
                .or(employeeRepository::findFirst)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Feedback feedback = new Feedback();
        feedback.setRoom(room);
        feedback.setEmployee(employee);
        feedback.setContent(content);
        feedback.setStatus(FeedbackStatus.CHO_XU_LY);
        feedback.setSentAt(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateIncidentStatus(Integer incidentId, FeedbackStatus status) {
        Feedback feedback = feedbackRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        feedback.setStatus(status);
        return feedbackRepository.save(feedback);
    }

    @Override
    public Contract updateOccupancy(String roomNumber, int peopleCount) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Contract contract = contractRepository.findActiveByRoomId(room.getId())
                .orElseThrow(() -> new IllegalArgumentException("Active contract not found"));
        contract.setOccupantCount(peopleCount);
        return contractRepository.save(contract);
    }

    @Override
    public Vehicle registerVehicle(String roomNumber, String vehicleType, String plateNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Contract contract = contractRepository.findActiveByRoomId(room.getId())
                .orElseThrow(() -> new IllegalArgumentException("Active contract not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setContract(contract);
        vehicle.setVehicleType(vehicleType);
        vehicle.setLicensePlate(plateNumber);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAllOrderByFloorAndRoomNumber();
    }

    @Override
    public List<Contract> getContracts() {
        return contractRepository.findAll();
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<UtilityReading> getUtilityReadings() {
        return utilityReadingRepository.findAll();
    }

    @Override
    public List<Invoice> getInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }

    private Invoice buildInvoice(Contract contract, YearMonth period, BigDecimal electricRate, BigDecimal waterRate,
                                 BigDecimal garbageRate, BigDecimal vehicleRate, Employee employee) {
        Invoice invoice = new Invoice();
        invoice.setContract(contract);
        invoice.setEmployee(employee);
        invoice.setMonth(period.getMonthValue());
        invoice.setYear(period.getYear());

        BigDecimal roomFee = defaultBigDecimal(contract.getRoom().getBaseRent());
        invoice.setRoomFee(roomFee);

        UtilityReading reading = utilityReadingRepository
                .findByRoomAndPeriod(contract.getRoom().getId(), period.getMonthValue(), period.getYear())
                .orElse(null);

        BigDecimal electricFee = BigDecimal.ZERO;
        BigDecimal waterFee = BigDecimal.ZERO;
        if (reading != null) {
            electricFee = electricRate.multiply(BigDecimal.valueOf(
                    Math.max(0, reading.getNewElectricNumber() - reading.getOldElectricNumber())));
            waterFee = waterRate.multiply(BigDecimal.valueOf(
                    Math.max(0, reading.getNewWaterNumber() - reading.getOldWaterNumber())));
        }

        long vehicleCount = vehicleRepository.countByContractId(contract.getId());
        BigDecimal parkingFee = vehicleRate.multiply(BigDecimal.valueOf(vehicleCount));

        invoice.setElectricFee(electricFee);
        invoice.setWaterFee(waterFee);
        invoice.setParkingFee(parkingFee);
        invoice.setGarbageFee(garbageRate);
        invoice.setTotal(roomFee.add(electricFee).add(waterFee).add(parkingFee).add(garbageRate));
        invoice.setPaid(Boolean.FALSE);
        invoice.setPaymentMethod(InvoicePaymentMethod.CHUA_THU);
        invoice.setCreatedAt(LocalDateTime.now());
        return invoice;
    }

    private BigDecimal getRate(RateType type) {
        return rateConfigRepository.findByType(type)
                .map(RateConfig::getUnitPrice)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}


