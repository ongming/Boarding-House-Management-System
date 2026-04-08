package com.example.house.service.impl.staff;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.Room;
import com.example.house.model.entity.UtilityReading;
import com.example.house.model.entity.Vehicle;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.InvoicePaymentMethod;
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
import com.example.house.service.impl.staff.support.BillingHandler;
import com.example.house.service.impl.staff.support.ContractHandler;
import com.example.house.service.impl.staff.support.IncidentHandler;
import com.example.house.model.dto.staff.ContractCreationRequest;
import com.example.house.service.staff.StaffDomainService;

import java.time.YearMonth;
import java.util.List;

public class StaffDomainServiceImpl implements StaffDomainService {
    private final RoomRepository roomRepository;
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final UtilityReadingRepository utilityReadingRepository;
    private final InvoiceRepository invoiceRepository;
    private final FeedbackRepository feedbackRepository;

    private final ContractHandler contractHandler;
    private final BillingHandler billingHandler;
    private final IncidentHandler incidentHandler;

    public StaffDomainServiceImpl() {
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

    public StaffDomainServiceImpl(RoomRepository roomRepository,
                                    TenantRepository tenantRepository,
                                    ContractRepository contractRepository,
                                    VehicleRepository vehicleRepository,
                                    UtilityReadingRepository utilityReadingRepository,
                                    InvoiceRepository invoiceRepository,
                                    FeedbackRepository feedbackRepository,
                                    RateConfigRepository rateConfigRepository,
                                    EmployeeRepository employeeRepository) {
        this.roomRepository = roomRepository;
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.utilityReadingRepository = utilityReadingRepository;
        this.invoiceRepository = invoiceRepository;
        this.feedbackRepository = feedbackRepository;

        this.contractHandler = new ContractHandler(
                roomRepository,
                tenantRepository,
                contractRepository,
                vehicleRepository,
                utilityReadingRepository
        );

        this.billingHandler = new BillingHandler(
                contractRepository,
                vehicleRepository,
                utilityReadingRepository,
                invoiceRepository,
                rateConfigRepository,
                employeeRepository
        );

        this.incidentHandler = new IncidentHandler(
                roomRepository,
                feedbackRepository,
                employeeRepository
        );
    }

    @Override
    public boolean isRoomAvailable(Integer roomId) {
        return contractHandler.isRoomAvailable(roomId);
    }

    @Override
    public Contract processContractCreation(ContractCreationRequest request) {
        return contractHandler.processContractCreation(request);
    }

    @Override
    public Contract updateContractMoveInDate(Integer contractId, java.time.LocalDate moveInDate) {
        return contractHandler.updateContractMoveInDate(contractId, moveInDate);
    }

    @Override
    public UtilityReading saveUtilityReading(String roomNumber, YearMonth period, int oldElectric, int newElectric,
                                             int oldWater, int newWater) {
        return contractHandler.saveUtilityReading(roomNumber, period, oldElectric, newElectric, oldWater, newWater);
    }

    @Override
    public List<Invoice> processAutoBilling(YearMonth period) {
        return billingHandler.processAutoBilling(period);
    }

    @Override
    public Invoice processPayment(Integer billId, InvoicePaymentMethod paymentType) {
        return billingHandler.processPayment(billId, paymentType);
    }

    @Override
    public Feedback saveIncident(Integer roomId, String content, Integer currentStaffId) {
        return incidentHandler.saveIncident(roomId, content, currentStaffId);
    }

    @Override
    public Feedback updateIncidentStatus(Integer incidentId, FeedbackStatus status) {
        return incidentHandler.updateIncidentStatus(incidentId, status);
    }

    @Override
    public Contract updateOccupancy(String roomNumber, int peopleCount) {
        return contractHandler.updateOccupancy(roomNumber, peopleCount);
    }

    @Override
    public Vehicle registerVehicle(String roomNumber, String vehicleType, String plateNumber) {
        return contractHandler.registerVehicle(roomNumber, vehicleType, plateNumber);
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
}
