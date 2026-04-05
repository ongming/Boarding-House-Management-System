package com.example.house.service.impl.admin;

import com.example.house.model.dto.admin.AdminRevenueReport;
import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.RevenueService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

public class RevenueServiceImpl implements RevenueService {
    private final AdminDomainService workflow;

    public RevenueServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public RevenueServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public AdminRevenueReport getRevenueReport(AdminRevenuePeriod period, int year) {
        return workflow.getRevenueReport(period, year);
    }
}
