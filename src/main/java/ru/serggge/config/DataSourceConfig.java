package ru.serggge.config;

import ru.serggge.App;
import java.util.Properties;
import static org.hibernate.cfg.JdbcSettings.*;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

// конфигурация проперти для EntityMangerFactory (для разных сред развёртывания)
public class DataSourceConfig {

    private final Profile profile;

    public DataSourceConfig() {
        // Datasource выбирается в зависимости от среды выполнения: DEVELOPMENT/PRODUCTION
        this.profile = App.profile;
    }

    public Properties getProperties() {
        return switch (profile) {
            case DEVELOPMENT -> developmentProperties();
            case PRODUCTION -> productionProperties();
        };
    }

    private Properties developmentProperties() {
        // конфигурация для среды DEVELOPMENT
        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, "org.h2.Driver");
        properties.put(JAKARTA_JDBC_URL, "jdbc:h2:mem:testdb");
        properties.put(JAKARTA_JDBC_USER, "sa");
        properties.put(JAKARTA_JDBC_PASSWORD, "sa");
        properties.put(DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(HBM2DDL_AUTO, "create-drop");
        properties.put(SHOW_SQL, "true");
        properties.put(FORMAT_SQL, "true");
        return properties;
    }

    private Properties productionProperties() {
        // конфигурация для среды PRODUCTION
        Properties properties = new Properties();
        properties.put(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:6543/astonhiberdb");
        properties.put(JAKARTA_JDBC_USER, "root");
        properties.put(JAKARTA_JDBC_PASSWORD, "root");
        properties.put(DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(HBM2DDL_AUTO, "update");
        return properties;
    }
}