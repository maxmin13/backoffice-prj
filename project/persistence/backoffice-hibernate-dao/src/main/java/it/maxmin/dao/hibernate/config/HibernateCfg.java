package it.maxmin.dao.hibernate.config;

import static org.hibernate.cfg.BatchSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.FetchSettings.MAX_FETCH_DEPTH;
import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.STATEMENT_FETCH_SIZE;
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
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(JndiDataSourceCfg.class)
@Configuration
@ComponentScan(basePackages = { "it.maxmin.dao.hibernate.impl.repo" }) 
@EnableTransactionManagement
public class HibernateCfg {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateCfg.class);

	private DataSource dataSource;

	@Autowired
	public HibernateCfg(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put(DIALECT, "org.hibernate.dialect.H2Dialect");
		hibernateProp.put(HBM2DDL_AUTO, "none");
		hibernateProp.put(FORMAT_SQL, true);
		hibernateProp.put(USE_SQL_COMMENTS, true);
		hibernateProp.put(HIGHLIGHT_SQL, true);
		hibernateProp.put(SHOW_SQL, true);
		hibernateProp.put(MAX_FETCH_DEPTH, 3);
		hibernateProp.put(STATEMENT_BATCH_SIZE, 10);
		hibernateProp.put(STATEMENT_FETCH_SIZE, 50);
		return hibernateProp;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		var sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("com.maxmin.domain.hibernate.entities");
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		var transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}
}
