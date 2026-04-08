package com.example.house.repository;

import com.example.house.model.entity.Tenant;

public interface TenantRepository {
    Tenant save(Tenant tenant);
}
