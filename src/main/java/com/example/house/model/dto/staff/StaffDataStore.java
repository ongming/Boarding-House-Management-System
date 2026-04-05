package com.example.house.model.dto.staff;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public interface StaffDataStore {
    class ContractItem {
        private final int id;
        private final String roomCode;
        private final String tenantName;
        private final double rentFee;
        private final double deposit;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String contractImageUrl;
        private final LocalDateTime createdAt;

        public ContractItem(int id, String roomCode, String tenantName, double rentFee, double deposit,
                            LocalDate startDate, LocalDate endDate, String contractImageUrl, LocalDateTime createdAt) {
            this.id = id;
            this.roomCode = roomCode;
            this.tenantName = tenantName;
            this.rentFee = rentFee;
            this.deposit = deposit;
            this.startDate = startDate;
            this.endDate = endDate;
            this.contractImageUrl = contractImageUrl;
            this.createdAt = createdAt;
        }

        public int id() { return id; }
        public String roomCode() { return roomCode; }
        public String tenantName() { return tenantName; }
        public double rentFee() { return rentFee; }
        public double deposit() { return deposit; }
        public LocalDate startDate() { return startDate; }
        public LocalDate endDate() { return endDate; }
        public String contractImageUrl() { return contractImageUrl; }
        public LocalDateTime createdAt() { return createdAt; }
    }

    class VehicleItem {
        private final int id;
        private final String roomCode;
        private final String vehicleType;
        private final String plateNumber;
        private final double monthlyFee;

        public VehicleItem(int id, String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
            this.id = id;
            this.roomCode = roomCode;
            this.vehicleType = vehicleType;
            this.plateNumber = plateNumber;
            this.monthlyFee = monthlyFee;
        }

        public int id() { return id; }
        public String roomCode() { return roomCode; }
        public String vehicleType() { return vehicleType; }
        public String plateNumber() { return plateNumber; }
        public double monthlyFee() { return monthlyFee; }
    }

    class OccupancyItem {
        private final String roomCode;
        private final int peopleCount;
        private final LocalDateTime updatedAt;

        public OccupancyItem(String roomCode, int peopleCount, LocalDateTime updatedAt) {
            this.roomCode = roomCode;
            this.peopleCount = peopleCount;
            this.updatedAt = updatedAt;
        }

        public String roomCode() { return roomCode; }
        public int peopleCount() { return peopleCount; }
        public LocalDateTime updatedAt() { return updatedAt; }
    }

    class MeterReadingItem {
        private final int id;
        private final String roomCode;
        private final YearMonth month;
        private final int oldElectric;
        private final int newElectric;
        private final int oldWater;
        private final int newWater;

        public MeterReadingItem(int id, String roomCode, YearMonth month, int oldElectric, int newElectric,
                                int oldWater, int newWater) {
            this.id = id;
            this.roomCode = roomCode;
            this.month = month;
            this.oldElectric = oldElectric;
            this.newElectric = newElectric;
            this.oldWater = oldWater;
            this.newWater = newWater;
        }

        public int id() { return id; }
        public String roomCode() { return roomCode; }
        public YearMonth month() { return month; }
        public int oldElectric() { return oldElectric; }
        public int newElectric() { return newElectric; }
        public int oldWater() { return oldWater; }
        public int newWater() { return newWater; }
    }

    class InvoiceItem {
        private final int id;
        private final String roomCode;
        private final YearMonth month;
        private final double roomFee;
        private final double electricFee;
        private final double waterFee;
        private final double vehicleFee;
        private final double garbageFee;
        private final double totalAmount;
        private final boolean paid;
        private final String paymentMethod;
        private final LocalDateTime createdAt;

        public InvoiceItem(int id, String roomCode, YearMonth month, double roomFee, double electricFee,
                           double waterFee, double vehicleFee, double garbageFee, double totalAmount,
                           boolean paid, String paymentMethod, LocalDateTime createdAt) {
            this.id = id;
            this.roomCode = roomCode;
            this.month = month;
            this.roomFee = roomFee;
            this.electricFee = electricFee;
            this.waterFee = waterFee;
            this.vehicleFee = vehicleFee;
            this.garbageFee = garbageFee;
            this.totalAmount = totalAmount;
            this.paid = paid;
            this.paymentMethod = paymentMethod;
            this.createdAt = createdAt;
        }

        public int id() { return id; }
        public String roomCode() { return roomCode; }
        public YearMonth month() { return month; }
        public double roomFee() { return roomFee; }
        public double electricFee() { return electricFee; }
        public double waterFee() { return waterFee; }
        public double vehicleFee() { return vehicleFee; }
        public double garbageFee() { return garbageFee; }
        public double totalAmount() { return totalAmount; }
        public boolean paid() { return paid; }
        public String paymentMethod() { return paymentMethod; }
        public LocalDateTime createdAt() { return createdAt; }
    }

    class FeedbackItem {
        private final int id;
        private final String roomCode;
        private final String title;
        private final String description;
        private final String priority;
        private final String status;
        private final LocalDateTime createdAt;

        public FeedbackItem(int id, String roomCode, String title, String description,
                            String priority, String status, LocalDateTime createdAt) {
            this.id = id;
            this.roomCode = roomCode;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.status = status;
            this.createdAt = createdAt;
        }

        public int id() { return id; }
        public String roomCode() { return roomCode; }
        public String title() { return title; }
        public String description() { return description; }
        public String priority() { return priority; }
        public String status() { return status; }
        public LocalDateTime createdAt() { return createdAt; }
    }
}
