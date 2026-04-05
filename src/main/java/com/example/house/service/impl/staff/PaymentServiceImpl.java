package com.example.house.service.impl.staff;

import com.example.house.model.entity.Invoice;
import com.example.house.model.enums.InvoicePaymentMethod;
import com.example.house.service.staff.PaymentService;
import com.example.house.service.staff.StaffDomainService;

public class PaymentServiceImpl implements PaymentService {
    private final StaffDomainService workflow;

    public PaymentServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public PaymentServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public Invoice processPayment(Integer billId, InvoicePaymentMethod paymentType) {
        return workflow.processPayment(billId, paymentType);
    }
}
