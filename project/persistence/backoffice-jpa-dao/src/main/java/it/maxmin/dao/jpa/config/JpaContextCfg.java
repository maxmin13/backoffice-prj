package it.maxmin.dao.jpa.config;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(JndiDataSourceCfg.class)
@Configuration
@ComponentScan(basePackages = { "it.maxmin.dao.jpa.impl.repo" })
@EnableTransactionManagement
public class JpaContextCfg {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaContextCfg.class);

	private DataSource dataSource;

	@Autowired
	public JpaContextCfg(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Properties jpaProperties() {
		Properties jpaProperties = new Properties();
		jpaProperties.put(HBM2DDL_AUTO, "none");
		jpaProperties.put(FORMAT_SQL, false);
		jpaProperties.put(USE_SQL_COMMENTS, false);
		jpaProperties.put(HIGHLIGHT_SQL, false);
		jpaProperties.put(SHOW_SQL, false);
		return jpaProperties;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		var factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan("it.maxmin.domain.jpa.entity");
		factory.setDataSource(dataSource);
		factory.setJpaProperties(jpaProperties());
		factory.setJpaVendorAdapter(jpaVendorAdapter());
		return factory;
	}
}
