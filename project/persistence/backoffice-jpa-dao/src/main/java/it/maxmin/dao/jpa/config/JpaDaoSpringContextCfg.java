package it.maxmin.dao.jpa.config;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import({ JndiDataSourceCfg.class, TransactionCfg.class })
@ComponentScan(basePackages = { "it.maxmin.dao.jpa.impl.repo", "it.maxmin.common.service.impl" })
@EnableTransactionManagement
public class JpaDaoSpringContextCfg {

	private DataSource dataSource;

	@Autowired
	public JpaDaoSpringContextCfg(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Properties jpaProperties() {
		Properties jpaProperties = new Properties();
		jpaProperties.put(HBM2DDL_AUTO, "none");
		jpaProperties.put(FORMAT_SQL, true);
		jpaProperties.put(USE_SQL_COMMENTS, true);
		jpaProperties.put(HIGHLIGHT_SQL, true);
		jpaProperties.put(SHOW_SQL, true);
		// Hibenate compliance to JPA spec
		jpaProperties.put("hibernate.jpa.compliance.proxy", true);
		jpaProperties.put("hibernate.jpa.compliance.closed", true);
		jpaProperties.put("hibernate.jpa.compliance.query", true);
		jpaProperties.put("hibernate.jpa.compliance.transaction", true);
		return jpaProperties;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		var factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan("it.maxmin.model.jpa.dao.entity");
		factory.setDataSource(dataSource);
		factory.setJpaProperties(jpaProperties());
		factory.setJpaVendorAdapter(jpaVendorAdapter());
		return factory;
	}
}
