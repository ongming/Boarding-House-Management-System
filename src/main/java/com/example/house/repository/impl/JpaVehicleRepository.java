package com.example.house.repository.impl;

import com.example.house.model.entity.Vehicle;
import com.example.house.repository.staff.VehicleRepository;

import java.util.List;

public class JpaVehicleRepository extends JpaRepositorySupport implements VehicleRepository {
    @Override
    public Vehicle save(Vehicle vehicle) {
        return inTransaction(em -> {
            if (vehicle.getId() == null) {
                em.persist(vehicle);
                return vehicle;
            }
            return em.merge(vehicle);
        });
    }

    @Override
    public List<Vehicle> findAll() {
        return withEntityManager(em -> em.createQuery(
                "SELECT v FROM Vehicle v ORDER BY v.id DESC", Vehicle.class)
                .getResultList());
    }

    @Override
    public List<Vehicle> findByContractId(Integer contractId) {
        return withEntityManager(em -> em.createQuery(
                "SELECT v FROM Vehicle v WHERE v.contract.id = :contractId", Vehicle.class)
                .setParameter("contractId", contractId)
                .getResultList());
    }

    @Override
    public long countByContractId(Integer contractId) {
        return withEntityManager(em -> em.createQuery(
                "SELECT COUNT(v) FROM Vehicle v WHERE v.contract.id = :contractId", Long.class)
                .setParameter("contractId", contractId)
                .getSingleResult());
    }
}

