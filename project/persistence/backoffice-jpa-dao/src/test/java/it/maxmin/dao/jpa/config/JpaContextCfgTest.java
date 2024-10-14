package it.maxmin.dao.jpa.config;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;

import it.maxmin.dao.jpa.DataSourceTestUtil;
import jakarta.persistence.EntityManagerFactory;

/**
 * Verifies that by loading JpaContextCfg.class, in the Spring context there is
 * a EntityManagerFactory and a TransactionManager objects.
 */
class JpaContextCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaContextCfgTest.class);

	private static DataSourceTestUtil dataSourceTestUtil = new DataSourceTestUtil();

	@Test
	void testEntityManagerFactory() throws IllegalStateException {

		var springCtx = new AnnotationConfigApplicationContext(JpaContextCfg.class);
		var entityManagerFactory = springCtx.getBean("entityManagerFactory", EntityManagerFactory.class);

		assertNotNull(entityManagerFactory);

		Map<String, Object> properties = entityManagerFactory.getProperties();

		assertEquals("none", properties.get(HBM2DDL_AUTO));
		assertEquals(false, properties.get(FORMAT_SQL));
		assertEquals(false, properties.get(USE_SQL_COMMENTS));
		assertEquals(false, properties.get(HIGHLIGHT_SQL));
		assertEquals(false, properties.get(SHOW_SQL));

		springCtx.close();
	}

	@Test
	void testTransactionManager() throws IllegalStateException, SQLException {

		var springCtx = new AnnotationConfigApplicationContext(JpaContextCfg.class);
		var transactionManager = springCtx.getBean("transactionManager", JpaTransactionManager.class);

		assertNotNull(transactionManager);

		DataSource dataSource = transactionManager.getDataSource();

		dataSourceTestUtil.testDataSource(dataSource);

		springCtx.close();
	}

}
