package com.example.house.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JpaUtil {
    private static final String PERSISTENCE_UNIT = "boarding-house-pu";
    private static EntityManagerFactory entityManagerFactory;

    private JpaUtil() {
    }

    public static synchronized EntityManager getEntityManager() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, buildOverrides());
        }
        return entityManagerFactory.createEntityManager();
    }

    public static synchronized void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    private static Map<String, Object> buildOverrides() {
        Map<String, Object> props = new HashMap<>();
        putIfPresent(props, "jakarta.persistence.jdbc.url", "DB_URL");
        putIfPresent(props, "jakarta.persistence.jdbc.user", "DB_USER");
        putIfPresent(props, "jakarta.persistence.jdbc.password", "DB_PASSWORD");
        putIfPresent(props, "jakarta.persistence.jdbc.driver", "DB_DRIVER");
        return props;
    }

    private static void putIfPresent(Map<String, Object> props, String jpaKey, String envKey) {
        String value = read(envKey);
        if (value != null) {
            props.put(jpaKey, value);
        }
    }

    private static String read(String key) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? null : value;
    }
}

