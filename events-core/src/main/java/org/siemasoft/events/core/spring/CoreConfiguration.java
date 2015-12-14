package org.siemasoft.events.core.spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Log4j2
@Configuration
@EnableJpaRepositories({PackagesToScan.JPA_REPOSITORY, PackagesToScan.JPA_ENTITY})
@ComponentScan(PackagesToScan.CORE)
public class CoreConfiguration {

    @Autowired
    private Environment environment;

    @Autowired
    private PropertyResolver propertyResolver;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(propertyResolver.getRequiredProperty("db_driver"));
        dataSourceConfig.setJdbcUrl(propertyResolver.getRequiredProperty("db_host") + propertyResolver.getRequiredProperty("db_database"));
        dataSourceConfig.setUsername(propertyResolver.getRequiredProperty("db_username"));
        dataSourceConfig.setPassword(propertyResolver.getRequiredProperty("db_password"));
        log.info("siema {},{},{},{}", dataSourceConfig.getDataSourceClassName(), dataSourceConfig.getJdbcUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(PackagesToScan.JPA_REPOSITORY, PackagesToScan.JPA_ENTITY);
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        boolean hibernate_show_sql = Boolean.parseBoolean(propertyResolver.getRequiredProperty("hibernate_show_sql"));
        jpaVendorAdapter.setShowSql(hibernate_show_sql);
        jpaVendorAdapter.setGenerateDdl(environment.getRequiredProperty("hibernate_generate_ddl", Boolean.class));
        jpaVendorAdapter.setDatabase(environment.getRequiredProperty("hibernate_database", Database.class));
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.ejb.naming_strategy", environment.getRequiredProperty("hibernate_ejb_naming_strategy"));
        jpaProperties.put("hibernate.format_sql", propertyResolver.getRequiredProperty("hibernate_format_sql"));
        jpaProperties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate_hbm2ddl_auto"));
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
