package com.example.house.service.impl.admin;

import com.example.house.model.entity.RateConfig;
import com.example.house.model.enums.RateType;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.RateConfigService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

import java.math.BigDecimal;
import java.util.List;

public class RateConfigServiceImpl implements RateConfigService {
    private final AdminDomainService workflow;

    public RateConfigServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public RateConfigServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<RateConfig> getRateConfigs() {
        return workflow.getRateConfigs();
    }

    @Override
    public RateConfig saveRateConfig(RateType type, BigDecimal unitPrice) {
        return workflow.saveRateConfig(type, unitPrice);
    }
}

