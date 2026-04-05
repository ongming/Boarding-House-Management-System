package com.example.house.service.impl.staff;

import com.example.house.model.entity.Invoice;
import com.example.house.service.staff.BillingService;
import com.example.house.service.staff.StaffDomainService;

import java.time.YearMonth;
import java.util.List;

public class BillingServiceImpl implements BillingService {
    private final StaffDomainService workflow;

    public BillingServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public BillingServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<Invoice> processAutoBilling(YearMonth period) {
        return workflow.processAutoBilling(period);
    }

    @Override
    public List<Invoice> getInvoices() {
        return workflow.getInvoices();
    }
}
