package com.example.house.repository;

import com.example.house.model.entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);

    Optional<Invoice> findById(Integer id);

    List<Invoice> findAll();

    boolean existsByContractAndPeriod(Integer contractId, int month, int year);

    List<Invoice> findByFilters(String roomNumber, Integer month, Integer year, Boolean paid);
}
