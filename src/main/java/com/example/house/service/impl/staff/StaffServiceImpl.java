package com.example.house.service.impl.staff;

import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import com.example.house.repository.impl.JpaStaffDataStore;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.YearMonth;

public class StaffServiceImpl implements StaffService {
    private final StaffDataStore dataStore;

    public StaffServiceImpl() {
        this(new JpaStaffDataStore());
    }

    public StaffServiceImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override public ObservableList<String> roomCodes() { return dataStore.roomCodes(); }
    @Override public ObservableList<StaffDataStore.ContractItem> contracts() { return dataStore.contracts(); }
    @Override public ObservableList<StaffDataStore.VehicleItem> vehicles() { return dataStore.vehicles(); }
    @Override public ObservableList<StaffDataStore.OccupancyItem> occupancies() { return dataStore.occupancies(); }
    @Override public ObservableList<StaffDataStore.MeterReadingItem> meterReadings() { return dataStore.meterReadings(); }
    @Override public ObservableList<StaffDataStore.InvoiceItem> invoices() { return dataStore.invoices(); }
    @Override public ObservableList<StaffDataStore.FeedbackItem> feedbacks() { return dataStore.feedbacks(); }

    @Override public StaffDataStore.ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit) {
        return dataStore.addContract(roomCode, tenantName, rentFee, deposit);
    }

    @Override public StaffDataStore.ContractItem addContractFull(String roomCode, String tenantName, String tenantCccd, String tenantPhone,
                                                                  LocalDate startDate, LocalDate moveInDate, LocalDate endDate,
                                                                  String contractImageUrl, int occupantCount, double rentFee, double deposit) {
        return dataStore.addContractFull(roomCode, tenantName, tenantCccd, tenantPhone,
            startDate, moveInDate, endDate, contractImageUrl, occupantCount, rentFee, deposit);
    }

    @Override public StaffDataStore.ContractItem updateContractMoveInDate(int contractId, LocalDate moveInDate) {
        return dataStore.updateContractMoveInDate(contractId, moveInDate);
    }

    @Override public StaffDataStore.VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
        return dataStore.addVehicle(roomCode, vehicleType, plateNumber, monthlyFee);
    }

    @Override public StaffDataStore.OccupancyItem upsertOccupancy(String roomCode, int peopleCount) {
        return dataStore.upsertOccupancy(roomCode, peopleCount);
    }

    @Override public StaffDataStore.MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric,
                                                                      int oldWater, int newWater) {
        return dataStore.addMeterReading(roomCode, month, oldElectric, newElectric, oldWater, newWater);
    }

    @Override public int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee) {
        return dataStore.generateInvoices(month, electricRate, waterRate, garbageFee);
    }

    @Override public StaffDataStore.InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod) {
        return dataStore.markInvoicePaid(invoiceId, paymentMethod);
    }

    @Override public StaffDataStore.FeedbackItem addFeedback(String roomCode, String title, String description, String priority) {
        return dataStore.addFeedback(roomCode, title, description, priority);
    }
}
