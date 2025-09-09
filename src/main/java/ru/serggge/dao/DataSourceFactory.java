package ru.serggge.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class DataSourceFactory {

    private static final Map<String, EntityManagerFactory> dataSources = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();

    private DataSourceFactory() {
    }

    public static EntityManagerFactory fromUnitName(String unitName) {
        if (dataSources.containsKey(unitName)) {
            return dataSources.get(unitName);
        } else {
            EntityManagerFactory emf;
            lock.lock();
            emf = dataSources.getOrDefault(unitName, Persistence.createEntityManagerFactory(unitName));
            dataSources.put(unitName, emf);
            lock.unlock();
            return emf;
        }
    }
}
