package com.example.house.service.impl.staff;

import com.example.house.repository.staff.feature.StaffBillingRepository;
import com.example.house.repository.staff.feature.StaffContractRepository;
import com.example.house.repository.staff.feature.StaffFeedbackRepository;
import com.example.house.repository.staff.feature.StaffMeterReadingRepository;
import com.example.house.repository.staff.feature.StaffOccupancyRepository;
import com.example.house.repository.staff.feature.StaffRoomCatalogRepository;
import com.example.house.repository.staff.feature.StaffVehicleRepository;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.YearMonth;

public class StaffServiceImpl implements StaffService {
    private final StaffRoomCatalogRepository roomCatalogRepository;
    private final StaffContractRepository contractRepository;
    private final StaffVehicleRepository vehicleRepository;
    private final StaffOccupancyRepository occupancyRepository;
    private final StaffMeterReadingRepository meterReadingRepository;
    private final StaffBillingRepository billingRepository;
    private final StaffFeedbackRepository feedbackRepository;

    public StaffServiceImpl(StaffRoomCatalogRepository roomCatalogRepository,
                            StaffContractRepository contractRepository,
                            StaffVehicleRepository vehicleRepository,
                            StaffOccupancyRepository occupancyRepository,
                            StaffMeterReadingRepository meterReadingRepository,
                            StaffBillingRepository billingRepository,
                            StaffFeedbackRepository feedbackRepository) {
        this.roomCatalogRepository = roomCatalogRepository;
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.occupancyRepository = occupancyRepository;
        this.meterReadingRepository = meterReadingRepository;
        this.billingRepository = billingRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override public ObservableList<String> roomCodes() { return roomCatalogRepository.roomCodes(); }
    @Override public ObservableList<StaffDataStore.ContractItem> contracts() { return contractRepository.contracts(); }
    @Override public ObservableList<StaffDataStore.VehicleItem> vehicles() { return vehicleRepository.vehicles(); }
    @Override public ObservableList<StaffDataStore.OccupancyItem> occupancies() { return occupancyRepository.occupancies(); }
    @Override public ObservableList<StaffDataStore.MeterReadingItem> meterReadings() { return meterReadingRepository.meterReadings(); }
    @Override public ObservableList<StaffDataStore.InvoiceItem> invoices() { return billingRepository.invoices(); }
    @Override public ObservableList<StaffDataStore.FeedbackItem> feedbacks() { return feedbackRepository.feedbacks(); }

    @Override public StaffDataStore.ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit) {
        return contractRepository.addContract(roomCode, tenantName, rentFee, deposit);
    }

    @Override public StaffDataStore.ContractItem addContractFull(String roomCode, String tenantName, String tenantCccd, String tenantPhone,
                                                                  LocalDate startDate, LocalDate moveInDate, LocalDate endDate,
                                                                  String contractImageUrl, int occupantCount, double rentFee, double deposit) {
        return contractRepository.addContractFull(roomCode, tenantName, tenantCccd, tenantPhone,
                startDate, moveInDate, endDate, contractImageUrl, occupantCount, rentFee, deposit);
    }

    @Override public StaffDataStore.ContractItem updateContractMoveInDate(int contractId, LocalDate moveInDate) {
        return contractRepository.updateContractMoveInDate(contractId, moveInDate);
    }

    @Override public StaffDataStore.VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
        return vehicleRepository.addVehicle(roomCode, vehicleType, plateNumber, monthlyFee);
    }

    @Override public StaffDataStore.OccupancyItem upsertOccupancy(String roomCode, int peopleCount) {
        return occupancyRepository.upsertOccupancy(roomCode, peopleCount);
    }

    @Override public StaffDataStore.MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric,
                                                                      int oldWater, int newWater) {
        return meterReadingRepository.addMeterReading(roomCode, month, oldElectric, newElectric, oldWater, newWater);
    }

    @Override public int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee) {
        return billingRepository.generateInvoices(month, electricRate, waterRate, garbageFee);
    }

    @Override public StaffDataStore.InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod) {
        return billingRepository.markInvoicePaid(invoiceId, paymentMethod);
    }

    @Override public StaffDataStore.FeedbackItem addFeedback(String roomCode, String title, String description, String priority) {
        return feedbackRepository.addFeedback(roomCode, title, description, priority);
    }
}
