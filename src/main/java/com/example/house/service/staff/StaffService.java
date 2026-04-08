package com.example.house.service.staff;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.YearMonth;

public interface StaffService {
    ObservableList<String> roomCodes();
    ObservableList<StaffDataStore.ContractItem> contracts();
    ObservableList<StaffDataStore.VehicleItem> vehicles();
    ObservableList<StaffDataStore.OccupancyItem> occupancies();
    ObservableList<StaffDataStore.MeterReadingItem> meterReadings();
    ObservableList<StaffDataStore.InvoiceItem> invoices();
    ObservableList<StaffDataStore.FeedbackItem> feedbacks();

    StaffDataStore.ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit);
    StaffDataStore.ContractItem addContractFull(String roomCode, String tenantName, String tenantCccd, String tenantPhone,
                                                LocalDate startDate, LocalDate moveInDate, LocalDate endDate,
                                                String contractImageUrl, int occupantCount, double rentFee, double deposit);
    StaffDataStore.ContractItem updateContractMoveInDate(int contractId, LocalDate moveInDate);
    StaffDataStore.VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee);
    StaffDataStore.OccupancyItem upsertOccupancy(String roomCode, int peopleCount);
    StaffDataStore.MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric,
                                                    int oldWater, int newWater);
    int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee);
    StaffDataStore.InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod);
    StaffDataStore.FeedbackItem addFeedback(String roomCode, String title, String description, String priority);
}
