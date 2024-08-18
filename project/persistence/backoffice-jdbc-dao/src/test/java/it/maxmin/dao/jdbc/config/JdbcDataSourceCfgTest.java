package it.maxmin.dao.jdbc.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.dao.jdbc.JdbcDaoTestUtil;
import it.maxmin.dao.jdbc.JdbcTestCfg;

/**
 * Verifies that by loading JndiDataSourceCfg.class, in the Spring context a
 * DataSource object is present. The test relies on simple-jndi library to
 * create a JNDI directory service in the background.
 */
class JdbcDataSourceCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDataSourceCfgTest.class);

	private static AnnotationConfigApplicationContext jdbcTestCfg;
	private static JdbcDaoTestUtil daoTestUtil;

	@BeforeAll
	public static void init() {
		jdbcTestCfg = new AnnotationConfigApplicationContext(JdbcTestCfg.class);
		daoTestUtil = jdbcTestCfg.getBean("daoTestUtil", JdbcDaoTestUtil.class);
	}

	@AfterAll
	public static void cleanUp() {
		jdbcTestCfg.close();
		daoTestUtil.stopTestDB();
	}

	@Test
	void testJndiDataSource() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(JndiDataSourceCfg.class);
		var dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);

		daoTestUtil.testDataSource(dataSource);

		springJdbcCtx.close();
	}

}
