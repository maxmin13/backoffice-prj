package it.maxmin.dao.jdbc.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.dao.jdbc.JdbcDataSourceTestUtil;

/**
 * Verifies that by loading JndiDataSourceCfg.class, in the Spring context a
 * DataSource object is present. The test relies on simple-jndi library to
 * create a JNDI directory service in the background with at datasource object in it.
 */
class JndiDataSourceCfgTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(JndiDataSourceCfgTest.class);

	private JdbcDataSourceTestUtil jdbcDataSourceTestUtil = new JdbcDataSourceTestUtil();

	@Test
	void testJndiDataSource() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(JndiDataSourceCfg.class);
		var dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);

		jdbcDataSourceTestUtil.testDataSource(dataSource);

		springJdbcCtx.close();
	}

}
