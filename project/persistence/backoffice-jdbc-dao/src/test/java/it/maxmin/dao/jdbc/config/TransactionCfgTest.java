package it.maxmin.dao.jdbc.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
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

		var springCtx = new AnnotationConfigApplicationContext(DataSourceCfg.class, TransactionCfg.class);
		var txManager = springCtx.getBean("platformTransactionManager", PlatformTransactionManager.class);

		assertNotNull(txManager);

		springCtx.close();
	}

	@Configuration
	public static class DataSourceCfg {

		@Bean
		public DataSource dataSource() throws NamingException {
			JndiTemplate jndiTemplate = new JndiTemplate();
			return (DataSource) jndiTemplate.lookup("datasources.h2");
		}

	}
}
