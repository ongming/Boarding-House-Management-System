package com.example.house.service.staff;

import com.example.house.model.entity.Invoice;
import com.example.house.model.enums.InvoicePaymentMethod;

public interface PaymentService {
    Invoice processPayment(Integer billId, InvoicePaymentMethod paymentType);
}


