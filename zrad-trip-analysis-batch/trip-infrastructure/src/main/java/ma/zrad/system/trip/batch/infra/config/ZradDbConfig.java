package ma.zrad.system.trip.batch.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static ma.zrad.system.trip.batch.infra.config.ZradDbConfig.BATCH_COMMON_PERSISTENCE_ENTITY_SCAN;
import static ma.zrad.system.trip.batch.infra.config.ZradDbConfig.ENTITY_MANAGER_FACTORY_REF;
import static ma.zrad.system.trip.batch.infra.config.ZradDbConfig.INFRA_PERSISTENCE_REPOSITORY_SCAN;
import static ma.zrad.system.trip.batch.infra.config.ZradDbConfig.TRANSACTION_MANAGER_REF;

@Configuration
@EnableTransactionManagement
@EntityScan(BATCH_COMMON_PERSISTENCE_ENTITY_SCAN)
@EnableJpaRepositories(
        basePackages = {INFRA_PERSISTENCE_REPOSITORY_SCAN},
        entityManagerFactoryRef = ENTITY_MANAGER_FACTORY_REF,
        transactionManagerRef = TRANSACTION_MANAGER_REF)
public class ZradDbConfig {

    public static final String ENTITY_MANAGER_FACTORY_REF = "zradEntityManager";
    public static final String TRANSACTION_MANAGER_REF = "zradTransactionManager";
    public static final String INFRA_PACK = "ma.zrad.system.trip.batch.infra";
    public static final String BATCH_COMMON_PACK = "ma.zrad.system.batch.common";
    public static final String BATCH_COMMON_PERSISTENCE_ENTITY_SCAN = BATCH_COMMON_PACK + ".persistence.jpa.entity";

    public static final String INFRA_PERSISTENCE_REPOSITORY_SCAN = INFRA_PACK + ".persistence.jpa.repository";

    @Autowired
    private Environment env;

    @Primary
    @Bean
    public DataSource zradDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl( env.getProperty("spring.datasource.url") );
        config.setUsername( env.getProperty("spring.datasource.username") );
        config.setPassword( env.getProperty("spring.datasource.password"));
        config.setDriverClassName( env.getProperty("spring.datasource.driverClassName"));
        return new HikariDataSource( config );
    }

    @Primary
    @Bean(ENTITY_MANAGER_FACTORY_REF)
    public LocalContainerEntityManagerFactoryBean zradEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(zradDataSource());
        em.setPackagesToScan(BATCH_COMMON_PERSISTENCE_ENTITY_SCAN);
        //em.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Charge les propriétés depuis le YAML
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));

        jpaProperties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        jpaProperties.put("hibernate.connection.driver_class", env.getProperty("spring.datasource.driverClassName"));

        //jpaProperties.put("hibernate.jdbc.lob.non_contextual_creation", true);
        em.setJpaPropertyMap(jpaProperties);

        return em;
    }

    @Primary
    @Bean(TRANSACTION_MANAGER_REF)
    public PlatformTransactionManager zradTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(zradEntityManager().getObject());
        return transactionManager;
    }

}
