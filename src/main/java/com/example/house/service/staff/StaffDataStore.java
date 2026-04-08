package com.example.house.service.staff;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.YearMonth;

public interface StaffDataStore extends com.example.house.model.dto.staff.StaffDataStore {
    ObservableList<String> roomCodes();
    ObservableList<ContractItem> contracts();
    ObservableList<VehicleItem> vehicles();
    ObservableList<OccupancyItem> occupancies();
    ObservableList<MeterReadingItem> meterReadings();
    ObservableList<InvoiceItem> invoices();
    ObservableList<FeedbackItem> feedbacks();

    ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit);

    ContractItem addContractFull(String roomCode,
                                 String tenantName,
                                 String tenantCccd,
                                 String tenantPhone,
                                 LocalDate startDate,
                                 LocalDate moveInDate,
                                 LocalDate endDate,
                                 String contractImageUrl,
                                 int occupantCount,
                                 double rentFee,
                                 double deposit);

    ContractItem updateContractMoveInDate(int contractId, LocalDate moveInDate);

    VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee);
    OccupancyItem upsertOccupancy(String roomCode, int peopleCount);

    MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric, int oldWater,
                                     int newWater);

    int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee);
    InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod);
    FeedbackItem addFeedback(String roomCode, String title, String description, String priority);
}
