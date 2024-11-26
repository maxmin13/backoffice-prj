package it.maxmin.dao.jpa.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.dao.jpa.JpaDataSourceTestUtil;

/**
 * Verifies that by loading JndiDataSourceCfg.class, in the Spring context a
 * DataSource object is present. The test relies on simple-jndi library to
 * create a JNDI directory service in the background with a "dataSource" object in it.
 */
class JndiDataSourceCfgTest {

	private JpaDataSourceTestUtil dataSourceTestUtil = new JpaDataSourceTestUtil();

	@Test
	void testJndiDataSource() throws SQLException, IllegalStateException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(JndiDataSourceCfg.class);
		var dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);

		dataSourceTestUtil.testDataSource(dataSource);
		
		springJdbcCtx.close();
	}

}
