package com.example.house.repository.impl;

import com.example.house.config.JpaUntil;
import com.example.house.model.entity.Account;
import com.example.house.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class JpaAccountRepository implements AccountRepository {

    @Override
    public Optional<Account> findByUsername(String username) {
        EntityManager entityManager = JpaUntil.getEntityManager();
        try {
            TypedQuery<Account> query = entityManager.createQuery(
                    "SELECT a FROM Account a WHERE LOWER(a.username) = LOWER(:username)",
                    Account.class
            );
            query.setParameter("username", username);
            return query.getResultStream().findFirst();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Account save(Account account) {
        EntityManager entityManager = JpaUntil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (account.getId() == null) {
                entityManager.persist(account);
            } else {
                account = entityManager.merge(account);
            }
            entityManager.getTransaction().commit();
            return account;
        } catch (RuntimeException ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void close() {
        JpaUntil.shutdown();
    }
}
