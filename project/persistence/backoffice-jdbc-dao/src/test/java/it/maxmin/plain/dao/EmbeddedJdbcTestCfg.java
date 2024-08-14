package it.maxmin.plain.dao;

import java.sql.Driver;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

/*
 * Starts an embedded MariaDB database and creates a Spring context for the unit tests.
 * */

public class EmbeddedJdbcTestCfg {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedJdbcTestCfg.class);

	@Bean
	public MariaDB4jSpringService mariaDB4jSpringService() {
		return new MariaDB4jSpringService();
	}
	
	@Bean
	public DaoTestUtil daoTestUtil() {
		return new DaoTestUtil();
	}

	@SuppressWarnings("unchecked")
	@Bean
	public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) {
		try {
			mariaDB4jSpringService.getDB().createDB("testDB");
		}
		catch (ManagedProcessException e) {
			LOGGER.error("Error creating DB", e);
			throw new DaoTestException("Error creating DB", e);
		}

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();
		
		var dataSource = new SimpleDriverDataSource();
		Class<? extends Driver> driver;
		try {
			driver = (Class<? extends Driver>) Class.forName("org.mariadb.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			LOGGER.error("Error loading DB driver", e);
			throw new DaoTestException("Error loading DB driver", e);
		}
		dataSource.setDriverClass(driver);
		dataSource.setUrl("jdbc:mariadb://localhost:"+config.getPort()+"/testDB");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		return dataSource;
	}

	@Bean
	public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
