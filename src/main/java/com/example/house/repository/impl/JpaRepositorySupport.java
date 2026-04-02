package com.example.house.repository.impl;

import com.example.house.config.JpaUntil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Function;

public abstract class JpaRepositorySupport {
    protected <T> T withEntityManager(Function<EntityManager, T> callback) {
        EntityManager em = JpaUntil.getEntityManager();
        try {
            return callback.apply(em);
        } finally {
            em.close();
        }
    }

    protected <T> T inTransaction(Function<EntityManager, T> callback) {
        EntityManager em = JpaUntil.getEntityManager();
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

