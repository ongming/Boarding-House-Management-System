package com.example.house.view.staff.data;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.YearMonth;

public interface StaffDataStore {
    record ContractItem(int id, String roomCode, String tenantName, double rentFee, double deposit, LocalDateTime createdAt) {
    }

    record VehicleItem(int id, String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
    }

    record OccupancyItem(String roomCode, int peopleCount, LocalDateTime updatedAt) {
    }

    record MeterReadingItem(int id, String roomCode, YearMonth month, int oldElectric, int newElectric, int oldWater,
                            int newWater) {
    }

    record InvoiceItem(int id, String roomCode, YearMonth month, double roomFee, double electricFee, double waterFee,
                       double vehicleFee, double garbageFee, double totalAmount, boolean paid, String paymentMethod,
                       LocalDateTime createdAt) {
    }

    record FeedbackItem(int id, String roomCode, String title, String description, String priority, String status,
                        LocalDateTime createdAt) {
    }

    ObservableList<ContractItem> contracts();

    ObservableList<VehicleItem> vehicles();

    ObservableList<OccupancyItem> occupancies();

    ObservableList<MeterReadingItem> meterReadings();

    ObservableList<InvoiceItem> invoices();

    ObservableList<FeedbackItem> feedbacks();

    ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit);

    VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee);

    OccupancyItem upsertOccupancy(String roomCode, int peopleCount);

    MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric, int oldWater,
                                     int newWater);

    int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee);

    InvoiceItem markInvoicePaid(int invoiceId, String paymentMethod);

    FeedbackItem addFeedback(String roomCode, String title, String description, String priority);
}

