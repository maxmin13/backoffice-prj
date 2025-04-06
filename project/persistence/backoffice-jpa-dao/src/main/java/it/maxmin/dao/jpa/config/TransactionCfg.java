package it.maxmin.dao.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class TransactionCfg {

	private LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Autowired
	public TransactionCfg(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Bean("platformTransactionManager")
	public PlatformTransactionManager platformTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return jpaTransactionManager;
	}
}
