package it.maxmin.dao.hibernate;

import static org.hibernate.cfg.BatchSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.FetchSettings.MAX_FETCH_DEPTH;
import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.STATEMENT_FETCH_SIZE;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

import java.sql.Driver;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

/*
 * Starts an embedded MariaDB database and creates a Spring context for the unit tests.
 * */

public class HibernateTestCfg {

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTestCfg.class);
	
	private DataSource dataSource;

	@Bean("mariaDB4jSpringService")
	public MariaDB4jSpringService mariaDB4jSpringService() {
		return new MariaDB4jSpringService();
	}

	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put(DIALECT, "org.hibernate.dialect.H2Dialect");
		hibernateProp.put(HBM2DDL_AUTO, "none");
		hibernateProp.put(FORMAT_SQL, true);
		hibernateProp.put(USE_SQL_COMMENTS, true);
		hibernateProp.put(HIGHLIGHT_SQL, true);
		hibernateProp.put(SHOW_SQL, true);
		hibernateProp.put(MAX_FETCH_DEPTH, 3);
		hibernateProp.put(STATEMENT_BATCH_SIZE, 10);
		hibernateProp.put(STATEMENT_FETCH_SIZE, 50);
		return hibernateProp;
	}

	@SuppressWarnings("unchecked")
	@Bean("dataSource")
	public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) {
		try {
			mariaDB4jSpringService.getDB().createDB("testDB");
		}
		catch (ManagedProcessException e) {
			throw new DaoTestException("Error creating the data source", e);
		}

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

		var ds = new SimpleDriverDataSource();
		Class<? extends Driver> driver;
		try {
			driver = (Class<? extends Driver>) Class.forName("org.mariadb.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			LOGGER.error("Error loading DB driver", e);
			throw new DaoTestException("Error loading DB driver", e);
		}
		ds.setDriverClass(driver);
		ds.setUrl("jdbc:mariadb://localhost:" + config.getPort() + "/testDB");
		ds.setUsername("root");
		ds.setPassword("root");
		dataSource = ds;
		return ds;
	}

	@Bean
	@DependsOn( "dataSource" )
	public SessionFactory sessionFactory() {
		return new LocalSessionFactoryBuilder(dataSource).scanPackages("com.maxmin.domain.hibernate.entities")
				.addProperties(hibernateProperties()).buildSessionFactory();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory());
	}

	@Bean("jdbcTemplate")
	public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Bean
	@DependsOn({"mariaDB4jSpringService", "jdbcTemplate"})
	public DaoTestUtil daoTestUtil() {
		return new DaoTestUtil();
	}
}
