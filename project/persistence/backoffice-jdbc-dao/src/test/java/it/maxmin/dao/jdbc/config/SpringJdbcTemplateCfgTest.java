package it.maxmin.dao.jdbc.config;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import it.maxmin.dao.jdbc.DaoTestUtil;
import it.maxmin.dao.jdbc.JdbcTestCfg;

/**
 * Verifies that by loading SpringJdbcTemplateCfg.class, in the Spring context a
 * JdbcTemplate object is present. The test relies on simple-jndi library to
 * create a JNDI directory service in the background.
 */
class SpringJdbcTemplateCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringJdbcTemplateCfgTest.class);

	private static AnnotationConfigApplicationContext jdbcTestCfg;
	private static DaoTestUtil daoTestUtil;

	@BeforeAll
	public static void init() {
		jdbcTestCfg = new AnnotationConfigApplicationContext(JdbcTestCfg.class);
		daoTestUtil = jdbcTestCfg.getBean("daoTestUtil", DaoTestUtil.class);
	}

	@AfterAll
	public static void cleanUp() {
		jdbcTestCfg.close();
		daoTestUtil.stopTestDB();
	}

	@Test
	void testSpringJdbcTemplate() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(SpringJdbcTemplateCfg.class);
		var jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", JdbcTemplate.class);

		daoTestUtil.testDataSource(jdbcTemplate.getDataSource());

		springJdbcCtx.close();
	}

}
