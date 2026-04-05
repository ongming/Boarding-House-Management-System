package com.example.house.model.dto.admin;

import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AdminDataStore {
    record RateItem(RateType type, double unitPrice) {
    }

    record RoomItem(Integer id, String roomNumber, Integer floor, double size, double baseRent, String furniture,
                    RoomStatus status) {
    }

    record StaffAccountItem(Integer id, String username, String fullName, String shiftSchedule) {
    }

    record RevenueRow(String label, double invoiceTotal, double compensationTotal, double total) {
    }

    record CheckoutItem(Integer contractId, String roomNumber, String tenantName, double deposit, LocalDate startDate) {
    }

    record CheckoutSummary(double deposit, double unpaidInvoices, double unpaidCompensations, double refundAmount) {
    }

    record FeedbackItem(Integer id, String roomNumber, String content, FeedbackStatus status, LocalDateTime sentAt) {
    }

    record InvoiceItem(Integer id, String roomNumber, int month, int year, double total, boolean paid,
                       String paymentMethod) {
    }
}
