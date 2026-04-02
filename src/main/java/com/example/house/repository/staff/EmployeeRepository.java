package com.example.house.repository.staff;

import com.example.house.model.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository {
    Optional<Employee> findById(Integer id);

    Optional<Employee> findFirst();
}

