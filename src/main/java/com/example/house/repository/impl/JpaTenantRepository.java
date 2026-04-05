package com.example.house.repository.impl;

import com.example.house.model.entity.Tenant;
import com.example.house.repository.staff.TenantRepository;

public class JpaTenantRepository extends JpaRepositorySupport implements TenantRepository {
    @Override
    public Tenant save(Tenant tenant) {
        return inTransaction(em -> {
            if (tenant.getId() == null) {
                em.persist(tenant);
                return tenant;
            }
            return em.merge(tenant);
        });
    }
}


