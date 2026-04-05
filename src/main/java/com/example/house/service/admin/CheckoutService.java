package com.example.house.service.admin;

import com.example.house.model.dto.admin.AdminCheckoutSummary;
import com.example.house.model.entity.Compensation;
import com.example.house.model.entity.Contract;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.RoomStatus;

import java.math.BigDecimal;
import java.util.List;

public interface CheckoutService {
    List<Contract> getActiveContracts();
    AdminCheckoutSummary buildCheckoutSummary(Integer contractId);
    Compensation addCompensation(Integer contractId, BigDecimal amount, String reason);
    void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod);
}
