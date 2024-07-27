package it.maxmin.plain.dao.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.maxmin.plain.dao.api.UserDao;
import it.maxmin.plain.dao.impl.UserDaoImpl;

@Import(JndiDataSourceCfg.class)
@Configuration
public class SpringDatasourceCfg {

	private static Logger LOGGER = LoggerFactory.getLogger(SpringDatasourceCfg.class);

	@Autowired
	DataSource dataSource;

	@Bean
	public UserDao userDao() {
		UserDaoImpl dao = new UserDaoImpl();
		dao.setDataSource(dataSource);
		return dao;
	}
}
