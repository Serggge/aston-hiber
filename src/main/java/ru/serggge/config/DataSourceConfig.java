package ru.serggge.config;

import lombok.RequiredArgsConstructor;
import ru.serggge.App;
import java.util.Properties;
import static org.hibernate.cfg.JdbcSettings.*;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

// конфигурация проперти для EntityMangerFactory (для разных сред развёртывания)
@RequiredArgsConstructor
public class DataSourceConfig {

    private final Profile profile;

    public Properties getProperties() {
        return switch (profile) {
            case DEVELOPMENT, TEST -> developmentProperties();
            case PRODUCTION -> productionProperties();
        };
    }

    private static Properties developmentProperties() {
        // конфигурация для среды DEVELOPMENT
        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, "org.h2.Driver");
        properties.put(JAKARTA_JDBC_URL, "jdbc:h2:mem:testdb;NON_KEYWORDS=USER");
        properties.put(JAKARTA_JDBC_USER, "sa");
        properties.put(JAKARTA_JDBC_PASSWORD, "sa");
        properties.put(HBM2DDL_AUTO, "create-drop");
        properties.put(SHOW_SQL, "true");
        properties.put(FORMAT_SQL, "true");
        return properties;
    }

    private static Properties productionProperties() {
        // конфигурация для среды PRODUCTION
        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(JAKARTA_JDBC_URL, "jdbc:postgresql://${POSTGRES_URL}:${POSTGRES_PORT}/${POSTGRES_DB}");
        properties.put(JAKARTA_JDBC_USER, "${POSTGRES_USER}");
        properties.put(JAKARTA_JDBC_PASSWORD, "${POSTGRES_PASSWORD}");
        properties.put(DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(HBM2DDL_AUTO, "update");
        return properties;
    }
}