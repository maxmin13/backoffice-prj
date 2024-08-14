package it.maxmin.plain.dao.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import(JndiDataSourceCfg.class)
public class SpringJdbcTemplateCfg {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringJdbcTemplateCfg.class);

	private DataSource dataSource;

	/**
	 * The DataSource object is used by Spring that handles Connection and ResultSet
	 * objects, no need to directly close them.
	 */
	@Autowired
	public SpringJdbcTemplateCfg(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * An instance of this template class is thread-safe once configured. The
	 * DataSource has to be set before using the instance.
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}
}
