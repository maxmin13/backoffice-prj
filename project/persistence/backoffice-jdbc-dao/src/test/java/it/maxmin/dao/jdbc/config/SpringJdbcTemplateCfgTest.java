package it.maxmin.dao.jdbc.config;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import it.maxmin.dao.jdbc.JdbcDataSourceTestUtil;
import it.maxmin.dao.jdbc.JdbcUnitTestContextCfg;


/**
 * Verifies that by loading SpringJdbcTemplateCfg.class, a JdbcTemplate object
 * is present in the Spring context. The test relies on a MariaDB database
 * running in the background (see MariaDB4jSpringService).
 */
class SpringJdbcTemplateCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringJdbcTemplateCfgTest.class);

	private static AnnotationConfigApplicationContext jdbcUnitTestContext;
	private static JdbcDataSourceTestUtil jdbcDataSourceTestUtil;

	@BeforeAll
	public static void init() {
		jdbcUnitTestContext = new AnnotationConfigApplicationContext(JdbcUnitTestContextCfg.class);
		jdbcDataSourceTestUtil = jdbcUnitTestContext.getBean("jdbcDataSourceTestUtil", JdbcDataSourceTestUtil.class);
	}

	@AfterAll
	public static void cleanUp() {
		jdbcUnitTestContext.close();
	}

	@Test
	void testSpringJdbcTemplate() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(SpringJdbcTemplateCfg.class);
		var jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", JdbcTemplate.class);

		jdbcDataSourceTestUtil.testDataSource(jdbcTemplate.getDataSource());

		springJdbcCtx.close();
	}

}
