package it.maxmin.dao.jdbc.ut.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.dao.jdbc.config.JndiDataSourceCfg;
import it.maxmin.dao.jdbc.util.JdbcDataSourceTestUtil;

/**
 * Verifies that by loading {@link JndiDataSourceCfgTest}, in the Spring context a
 * DataSource object is present. The test relies on simple-jndi library to
 * create a JNDI directory service in the background with a "dataSource" object
 * in it.
 */
class JndiDataSourceCfgTest {

	private JdbcDataSourceTestUtil dataSourceTestUtil = new JdbcDataSourceTestUtil();

	@Test
	void testJndiDataSource() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(JndiDataSourceCfg.class);
		var dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);

		dataSourceTestUtil.testDataSource(dataSource);
		
		springJdbcCtx.close();
	}

}
