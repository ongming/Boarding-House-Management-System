package com.example.house.service.admin;

import com.example.house.model.entity.Account;
import com.example.house.model.entity.Compensation;
import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Employee;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.RateConfig;
import com.example.house.model.entity.Room;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AdminWorkflowService {
    List<RateConfig> getRateConfigs();

    RateConfig saveRateConfig(RateType type, BigDecimal unitPrice);

    List<Room> getRooms();

    Room saveRoom(Room room);

    void deleteRoom(Integer roomId);

    List<Employee> getEmployees();

    Account createStaffAccount(String username, String rawPassword, String fullName, String shiftSchedule);

    AdminRevenueReport getRevenueReport(AdminRevenuePeriod period, int year);

    List<Contract> getActiveContracts();

    AdminCheckoutSummary buildCheckoutSummary(Integer contractId);

    Compensation addCompensation(Integer contractId, BigDecimal amount, String reason);

    void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod);

    List<Feedback> getFeedbacks();

    List<Feedback> getFeedbacksByStatus(FeedbackStatus status);

    Feedback updateFeedbackStatus(Integer id, FeedbackStatus status);

    List<Invoice> findInvoices(String roomNumber, Integer month, Integer year, Boolean paid);
}
