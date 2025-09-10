package ru.serggge.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ru.serggge.config.DataSourceConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

// фабрика EntityManagerFactory
public class DataSourceFactory {

    private static final Map<String, EntityManagerFactory> dataSources = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();

    private DataSourceFactory() {
    }

    public static EntityManagerFactory fromUnitName(String unitName) {
        // по аргументу unitName предоставляет соответствующий EntityMangerFactory
        if (dataSources.containsKey(unitName)) {
            return dataSources.get(unitName);
        } else {
            EntityManagerFactory emf;
            lock.lock();
            // получаем проперти для EntityMangerFactory из конфига
            Properties dataSourceProperties = new DataSourceConfig().getProperties();
            emf = dataSources.getOrDefault(unitName,
                    Persistence.createEntityManagerFactory(unitName, dataSourceProperties));
            dataSources.put(unitName, emf);
            lock.unlock();
            return emf;
        }
    }
}
