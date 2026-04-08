package com.example.house.repository.impl;

import com.example.house.model.entity.Employee;
import com.example.house.repository.EmployeeRepository;

import java.util.Optional;

public class JpaEmployeeRepository extends JpaRepositorySupport implements EmployeeRepository {
    @Override
    public Optional<Employee> findById(Integer id) {
        return withEntityManager(em -> Optional.ofNullable(em.find(Employee.class, id)));
    }

    @Override
    public Optional<Employee> findFirst() {
        return withEntityManager(em -> em.createQuery(
                "SELECT e FROM Employee e ORDER BY e.id ASC", Employee.class)
                .setMaxResults(1)
                .getResultStream()
                .findFirst());
    }

    @Override
    public Employee save(Employee employee) {
        return inTransaction(em -> {
            if (employee.getId() == null) {
                em.persist(employee);
                return employee;
            }
            return em.merge(employee);
        });
    }

    @Override
    public java.util.List<Employee> findAll() {
        return withEntityManager(em -> em.createQuery(
                "SELECT e FROM Employee e ORDER BY e.id DESC", Employee.class)
                .getResultList());
    }
}


