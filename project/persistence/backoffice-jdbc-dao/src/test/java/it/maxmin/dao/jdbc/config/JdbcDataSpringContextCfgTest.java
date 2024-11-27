package it.maxmin.dao.jdbc.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.JdbcDataSourceTestUtil;

/**
 * Verifies that by loading {@link JdbcDaoSpringContextCfg}, a {@link JdbcTemplate} and
 * a {@link PropertySourcesPlaceholderConfigurer} object are loaded in the Spring
 * context. The test relies on simple-jndi library to create a JNDI directory
 * service in the background with a "dataSource" object in it.
 */
class JdbcDataSpringContextCfgTest {

	@Test
	void testJdbcTemplate() throws SQLException, IllegalStateException {

		var springCtx = new AnnotationConfigApplicationContext(JdbcDaoSpringContextCfg.class);
		var jdbcTemplate = springCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);

		JdbcDataSourceTestUtil dataSourceTestUtil = new JdbcDataSourceTestUtil();
		dataSourceTestUtil.testDataSource(jdbcTemplate.getJdbcTemplate().getDataSource());

		springCtx.close();
	}
	

	@Test
	void testPropertySourcesPlaceholderConfigurer() {

		var springCtx = new AnnotationConfigApplicationContext(JdbcDaoSpringContextCfg.class);
		var propertySourcesPlaceholderConfigurer = springCtx.getBean("propertySourcesPlaceholderConfigurer", PropertySourcesPlaceholderConfigurer.class);

		assertNotNull(propertySourcesPlaceholderConfigurer);

		springCtx.close();
	}

}
