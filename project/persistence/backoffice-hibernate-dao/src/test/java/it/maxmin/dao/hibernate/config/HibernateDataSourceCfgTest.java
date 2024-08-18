package it.maxmin.dao.hibernate.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.dao.hibernate.HibernateDaoTestUtil;
import it.maxmin.dao.hibernate.HibernateTestCfg;

/**
 * Verifies that by loading JndiDataSourceCfg.class, in the Spring context a
 * DataSource object is present. The test relies on simple-jndi library to
 * create a JNDI directory service in the background.
 */
class HibernateDataSourceCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateDataSourceCfgTest.class);

	private static AnnotationConfigApplicationContext hibernateTestCfg;
	private static HibernateDaoTestUtil daoTestUtil;

	@BeforeAll
	public static void init() {
		hibernateTestCfg = new AnnotationConfigApplicationContext(HibernateTestCfg.class);
		daoTestUtil = hibernateTestCfg.getBean("daoTestUtil", HibernateDaoTestUtil.class);
	}

	@AfterAll
	public static void cleanUp() {
		daoTestUtil.stopTestDB();
		hibernateTestCfg.close();
	}

	@Test
	void testJndiDataSource() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(JndiDataSourceCfg.class);
		var dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);

		daoTestUtil.testDataSource(dataSource);

		springJdbcCtx.close();
	}

}
