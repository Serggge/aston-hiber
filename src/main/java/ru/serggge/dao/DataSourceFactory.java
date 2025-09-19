package ru.serggge.dao;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import ru.serggge.config.DataSourceConfig;
import ru.serggge.config.Profile;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

// фабрика EntityManagerFactory
public class DataSourceFactory {

    private static final Map<Class<?>, EntityManagerFactory> dataSources = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();

    private DataSourceFactory() {
    }

    public static EntityManagerFactory forEntityClass(Class<?> entityClass, Profile profile) {
        // по аргументу entityClass предоставляет соответствующий EntityMangerFactory
        if (dataSources.containsKey(entityClass)) {
            return dataSources.get(entityClass);
        } else {
            try {
                lock.lock();
                Properties dataSourceProperties = DataSourceConfig.getProperties(profile);
                EntityManagerFactory emf = dataSources.getOrDefault(entityClass,
                        new Configuration()
                                .setProperties(dataSourceProperties) // проперти из конфига
                                .addAnnotatedClass(entityClass)
                                .buildSessionFactory()
                );
                dataSources.put(entityClass, emf);
                return emf;
            } finally {
                lock.unlock();
            }
        }
    }
}