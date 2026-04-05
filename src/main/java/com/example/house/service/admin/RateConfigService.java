package com.example.house.service.admin;

import com.example.house.model.entity.RateConfig;
import com.example.house.model.enums.RateType;

import java.math.BigDecimal;
import java.util.List;

public interface RateConfigService {
    List<RateConfig> getRateConfigs();
    RateConfig saveRateConfig(RateType type, BigDecimal unitPrice);
}


