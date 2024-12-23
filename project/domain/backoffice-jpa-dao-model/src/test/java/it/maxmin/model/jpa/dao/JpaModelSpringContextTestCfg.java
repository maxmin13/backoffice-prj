package it.maxmin.model.jpa.dao;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

import java.sql.Driver;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.model.jpa.dao.exception.JpaModelTestException;

/**
 * Starts an embedded MariaDB database and creates a Spring context for the unit
 * tests.
 */

@Configuration
@EnableTransactionManagement
public class JpaModelSpringContextTestCfg {
	
	private DataSource dataSource;
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Bean
	@DependsOn("dataSource")
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	@DependsOn("dataSource")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		var factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan("it.maxmin.model.jpa.dao.entity");
		factory.setDataSource(dataSource);
		factory.setJpaProperties(jpaProperties());
		factory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactory = factory;
		return factory;
	}
	
	@Bean
	@DependsOn("dataSource")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return transactionManager;
	}

	@Bean
	public MariaDB4jSpringService mariaDB4jSpringService() {
		return new MariaDB4jSpringService();
	}

	@SuppressWarnings("unchecked")
	@Bean(name = "dataSource")
	public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) {
		try {
			mariaDB4jSpringService.getDB().createDB("testDB");
		} catch (ManagedProcessException e) {
			throw new JpaModelTestException("Error creating the data source", e);
		}

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

		var ds = new SimpleDriverDataSource();
		Class<? extends Driver> driver;
		try {
			driver = (Class<? extends Driver>) Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new JpaModelTestException("Error loading DB driver", e);
		}
		ds.setDriverClass(driver);
		ds.setUrl("jdbc:mariadb://localhost:" + config.getPort() + "/testDB");
		ds.setUsername("root");
		ds.setPassword("root");
		dataSource = ds;
		return ds;
	}
	
	private Properties jpaProperties() {
		Properties jpaProperties = new Properties();
		jpaProperties.put(HBM2DDL_AUTO, "none");
		jpaProperties.put(FORMAT_SQL, true);
		jpaProperties.put(USE_SQL_COMMENTS, true);
		jpaProperties.put(HIGHLIGHT_SQL, true);
		jpaProperties.put(SHOW_SQL, true);
		return jpaProperties;
	}
}
