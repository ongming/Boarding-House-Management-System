package com.example.house.service.impl.admin;

import com.example.house.model.entity.Invoice;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.InvoiceQueryService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

import java.util.List;

public class InvoiceQueryServiceImpl implements InvoiceQueryService {
    private final AdminDomainService workflow;

    public InvoiceQueryServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public InvoiceQueryServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<Invoice> findInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        return workflow.findInvoices(roomNumber, month, year, paid);
    }
}

