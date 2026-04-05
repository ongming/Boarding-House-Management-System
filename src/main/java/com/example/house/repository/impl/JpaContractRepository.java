package com.example.house.repository.impl;

import com.example.house.config.JpaUtil;
import com.example.house.model.entity.Contract;
import com.example.house.model.enums.ContractStatus;
import com.example.house.repository.staff.ContractRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JpaContractRepository implements ContractRepository {
    @Override
    public Contract save(Contract contract) {
        return inTransaction(em -> {
            if (contract.getId() == null) {
                em.persist(contract);
                return contract;
            }
            return em.merge(contract);
        });
    }

    @Override
    public Optional<Contract> findById(Integer id) {
        return withEntityManager(em -> Optional.ofNullable(em.find(Contract.class, id)));
    }

    @Override
    public List<Contract> findAll() {
        return withEntityManager(em -> em.createQuery("SELECT c FROM Contract c ORDER BY c.id DESC", Contract.class)
                .getResultList());
    }

    @Override
    public List<Contract> findActiveContracts() {
        return withEntityManager(em -> em.createQuery("SELECT c FROM Contract c WHERE c.status = :status", Contract.class)
                .setParameter("status", ContractStatus.HIEU_LUC)
                .getResultList());
    }

    @Override
    public Optional<Contract> findActiveByRoomId(Integer roomId) {
        return withEntityManager(em -> em.createQuery(
                        "SELECT c FROM Contract c WHERE c.room.id = :roomId AND c.status = :status", Contract.class)
                .setParameter("roomId", roomId)
                .setParameter("status", ContractStatus.HIEU_LUC)
                .getResultStream()
                .findFirst());
    }

    private <T> T withEntityManager(Function<EntityManager, T> callback) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return callback.apply(em);
        } finally {
            em.close();
        }
    }

    private <T> T inTransaction(Function<EntityManager, T> callback) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = callback.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
