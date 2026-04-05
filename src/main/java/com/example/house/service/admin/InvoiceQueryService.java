package com.example.house.service.admin;

import com.example.house.model.entity.Invoice;

import java.util.List;

public interface InvoiceQueryService {
    List<Invoice> findInvoices(String roomNumber, Integer month, Integer year, Boolean paid);
}


