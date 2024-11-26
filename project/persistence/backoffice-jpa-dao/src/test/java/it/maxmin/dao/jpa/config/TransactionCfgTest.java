package it.maxmin.dao.jpa.config;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Verifies that by loading {@link TransactionCfg} context, a
 * {@link PlatformTransactionManager} object is in the Spring context. The test
 * relies on simple-jndi library to create a JNDI directory service in the
 * background with a "dataSource" object in it.
 * 
 */
class TransactionCfgTest {

	@Test
	void testSpringJdbcTemplate() {

		var springCtx = new AnnotationConfigApplicationContext(EntityManagerFactoryCfg.class, TransactionCfg.class);
		var txManager = springCtx.getBean("transactionManager", JpaTransactionManager.class);

		assertNotNull(txManager);

		springCtx.close();
	}

	@Configuration
	public static class EntityManagerFactoryCfg {
		
		private Properties jpaProperties() {
			Properties jpaProperties = new Properties();
			jpaProperties.put(HBM2DDL_AUTO, "none");
			jpaProperties.put(FORMAT_SQL, false);
			jpaProperties.put(USE_SQL_COMMENTS, false);
			jpaProperties.put(HIGHLIGHT_SQL, false);
			jpaProperties.put(SHOW_SQL, false);
			return jpaProperties;
		}

		private JpaVendorAdapter jpaVendorAdapter() {
			return new HibernateJpaVendorAdapter();
		}
		
		private DataSource dataSource() throws NamingException {
			JndiTemplate jndiTemplate = new JndiTemplate();
			return (DataSource) jndiTemplate.lookup("testds.ds");
		}

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
			var factory = new LocalContainerEntityManagerFactoryBean();
			factory.setPackagesToScan("it.maxmin.model.jpa.dao.entity");
			factory.setDataSource(dataSource());
			factory.setJpaProperties(jpaProperties());
			factory.setJpaVendorAdapter(jpaVendorAdapter());
			return factory;
		}
	}
}
