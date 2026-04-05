package com.example.house.service.impl.admin;

import com.example.house.model.dto.admin.AdminCheckoutSummary;
import com.example.house.model.entity.Compensation;
import com.example.house.model.entity.Contract;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.CheckoutService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutServiceImpl implements CheckoutService {
    private final AdminDomainService workflow;

    public CheckoutServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public CheckoutServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<Contract> getActiveContracts() {
        return workflow.getActiveContracts();
    }

    @Override
    public AdminCheckoutSummary buildCheckoutSummary(Integer contractId) {
        return workflow.buildCheckoutSummary(contractId);
    }

    @Override
    public Compensation addCompensation(Integer contractId, BigDecimal amount, String reason) {
        return workflow.addCompensation(contractId, amount, reason);
    }

    @Override
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        workflow.approveCheckout(contractId, roomStatus, paymentMethod);
    }
}
