package com.example.house.repository.impl;

import com.example.house.model.entity.Compensation;
import com.example.house.repository.staff.CompensationRepository;

import java.util.List;
import java.util.Optional;

public class JpaCompensationRepository extends JpaRepositorySupport implements CompensationRepository {
    @Override
    public Compensation save(Compensation compensation) {
        return inTransaction(em -> {
            if (compensation.getId() == null) {
                em.persist(compensation);
                return compensation;
            }
            return em.merge(compensation);
        });
    }

    @Override
    public Optional<Compensation> findById(Integer id) {
        return withEntityManager(em -> Optional.ofNullable(em.find(Compensation.class, id)));
    }

    @Override
    public List<Compensation> findByContractId(Integer contractId) {
        return withEntityManager(em -> em.createQuery(
                "SELECT c FROM Compensation c WHERE c.contract.id = :contractId ORDER BY c.id DESC",
                Compensation.class)
                .setParameter("contractId", contractId)
                .getResultList());
    }

    @Override
    public List<Compensation> findByContractIdAndCollected(Integer contractId, boolean collected) {
        return withEntityManager(em -> em.createQuery(
                "SELECT c FROM Compensation c WHERE c.contract.id = :contractId AND c.collected = :collected ORDER BY c.id DESC",
                Compensation.class)
                .setParameter("contractId", contractId)
                .setParameter("collected", collected)
                .getResultList());
    }
}

