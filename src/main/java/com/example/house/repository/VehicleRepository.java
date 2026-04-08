package com.example.house.repository;

import com.example.house.model.entity.Vehicle;

import java.util.List;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);

    List<Vehicle> findAll();

    List<Vehicle> findByContractId(Integer contractId);

    long countByContractId(Integer contractId);

    int deleteByContractId(Integer contractId);
}
