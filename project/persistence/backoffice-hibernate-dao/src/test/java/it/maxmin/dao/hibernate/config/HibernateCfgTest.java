package it.maxmin.dao.hibernate.config;

import static org.hibernate.cfg.BatchSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.FetchSettings.MAX_FETCH_DEPTH;
import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.STATEMENT_FETCH_SIZE;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import it.maxmin.dao.hibernate.DataSourceTestUtil;
import it.maxmin.dao.hibernate.UnitTestContextCfg;

/**
 * Verifies that by loading HibernateCfg.class, a SessionFactory and a TransactionManager 
 * objects are present in the Spring context. The test relies on a MariaDB database 
 * running in the background (see MariaDB4jSpringService).
 */
class HibernateCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateCfgTest.class);

	private static AnnotationConfigApplicationContext unitTestContext;
	private static DataSourceTestUtil dataSourceTestUtil;

	@BeforeAll
	public static void init() {
		unitTestContext = new AnnotationConfigApplicationContext(UnitTestContextCfg.class);
		dataSourceTestUtil = unitTestContext.getBean("dataSourceTestUtil", DataSourceTestUtil.class);
	}

	@AfterAll
	public static void cleanUp() {
		unitTestContext.close();
	}

	@Test
	void testSessionFactory() throws IllegalStateException {

		var springCtx = new AnnotationConfigApplicationContext(HibernateCfg.class);
		var sessionFactory = springCtx.getBean("sessionFactory", SessionFactory.class);

		assertNotNull(sessionFactory);

		Map<String, Object> properties = sessionFactory.getProperties();

		assertEquals("org.hibernate.dialect.H2Dialect", properties.get(DIALECT));
		assertEquals("none", properties.get(HBM2DDL_AUTO));
		assertEquals(true, properties.get(FORMAT_SQL));
		assertEquals(true, properties.get(USE_SQL_COMMENTS));
		assertEquals(true, properties.get(HIGHLIGHT_SQL));
		assertEquals(true, properties.get(SHOW_SQL));
		assertEquals(3, properties.get(MAX_FETCH_DEPTH));
		assertEquals(10, properties.get(STATEMENT_BATCH_SIZE));
		assertEquals(50, properties.get(STATEMENT_FETCH_SIZE));

		springCtx.close();
	}

	@Test
	void testTransactionManager() throws IllegalStateException, SQLException {

		var springCtx = new AnnotationConfigApplicationContext(HibernateCfg.class);
		var transactionManager = springCtx.getBean("transactionManager", HibernateTransactionManager.class);

		assertNotNull(transactionManager);

		DataSource dataSource = transactionManager.getDataSource();

		dataSourceTestUtil.testDataSource(dataSource);

		springCtx.close();
	}

}
