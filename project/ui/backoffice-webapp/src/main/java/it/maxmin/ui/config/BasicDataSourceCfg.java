package it.maxmin.ui.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;

@Configuration
//@PropertySource("classpath:db/jdbc.properties")
@PropertySource("classpath:db/jndi.properties")
public class BasicDataSourceCfg {
	
	private static Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceCfg.class);

//	@Value("${jdbc.driverClassName}")
//	private String driverClassName;

//	@Value("${jdbc.url}")
//	private String url;

//	@Value("${jdbc.username}")
//	private String username;

//	@Value("${jdbc.password}")
//	private String password;
	
	@Value("${jndi.dataSourceName}")
	private String dataSourceName;

	/*@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		try {
			var hc = new HikariConfig();
			hc.setJdbcUrl(url);
			hc.setDriverClassName(driverClassName);
			hc.setUsername(username);
			hc.setPassword(password);
			var dataSource = new HikariDataSource(hc);
			dataSource.setMaximumPoolSize(25); 
			return dataSource;
		} catch (Exception e) {
			LOGGER.error("Hikari DataSource bean cannot be created!", e);
			return null;
		}
	}*/
	
    @Bean
    DataSource dataSource() {
        JndiTemplate jndi = new JndiTemplate();
        try {
            return jndi.lookup("java:comp/env/" + dataSourceName, DataSource.class);
        } 
        catch (NamingException e) {
        	LOGGER.error("NamingException for java:comp/env/jdbc/musicdb", e);
        	return null;
        }
    }
}
