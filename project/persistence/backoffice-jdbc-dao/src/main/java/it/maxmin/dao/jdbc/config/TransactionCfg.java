package it.maxmin.dao.jdbc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class TransactionCfg {

	private DataSource dataSource;

	@Autowired
	public TransactionCfg(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean("platformTransactionManager")
	public PlatformTransactionManager platformTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
}
