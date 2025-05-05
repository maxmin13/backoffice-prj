package it.maxmin.dao.jpa.config;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;

/**
 * Starts an embedded MariaDB database and creates a Spring context for the unit
 * tests.
 */

@Configuration
@Import(JpaDaoSpringContextCfg.class)
@ComponentScan(basePackages = { "it.maxmin.dao.jpa.common" })
public class JpaDaoUnitTestCfg {

	@Bean
	public MariaDB4jSpringService mariaDB4jSpringService() {
		return new MariaDB4jSpringService();
	}

	/**
	 * overrides {@link JndiDataSourceCfg} dataSource() method
	 */
	@SuppressWarnings("unchecked")
	@Bean
	public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) {
		try {
			mariaDB4jSpringService.getDB().createDB("testDB");
		}
		catch (ManagedProcessException e) {
			throw new JpaDaoTestException("Error creating the data source", e);
		}

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

		var ds = new SimpleDriverDataSource();
		Class<? extends Driver> driver;
		try {
			driver = (Class<? extends Driver>) Class.forName("org.mariadb.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			throw new JpaDaoTestException("Error loading DB driver", e);
		}
		ds.setDriverClass(driver);
		ds.setUrl("jdbc:mariadb://localhost:" + config.getPort() + "/testDB");
		ds.setUsername("root");
		ds.setPassword("root");
		return ds;
	}

	@Bean
	public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

}
