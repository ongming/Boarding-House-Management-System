package com.example.house.repository.impl;

import com.example.house.model.entity.Invoice;
import com.example.house.repository.staff.InvoiceRepository;

import java.util.List;
import java.util.Optional;

public class JpaInvoiceRepository extends JpaRepositorySupport implements InvoiceRepository {
    @Override
    public Invoice save(Invoice invoice) {
        return inTransaction(em -> {
            if (invoice.getId() == null) {
                em.persist(invoice);
                return invoice;
            }
            return em.merge(invoice);
        });
    }

    @Override
    public Optional<Invoice> findById(Integer id) {
        return withEntityManager(em -> Optional.ofNullable(em.find(Invoice.class, id)));
    }

    @Override
    public List<Invoice> findAll() {
        return withEntityManager(em -> em.createQuery(
                        "SELECT i FROM Invoice i ORDER BY i.id DESC", Invoice.class)
                .getResultList());
    }

    @Override
    public boolean existsByContractAndPeriod(Integer contractId, int month, int year) {
        return withEntityManager(em -> em.createQuery(
                        "SELECT COUNT(i) FROM Invoice i WHERE i.contract.id = :contractId AND i.month = :month AND i.year = :year",
                        Long.class)
                .setParameter("contractId", contractId)
                .setParameter("month", month)
                .setParameter("year", year)
                .getSingleResult() > 0);
    }
}

