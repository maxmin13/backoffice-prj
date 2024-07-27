package it.maxmin.plain.dao.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;

import javax.sql.DataSource;

/**
 * Assume Datasource is configured as a resource in an application such as Apache Tomcat
 * {@code
    <Resource name="jdbc/musicdb"
    auth="Container"
    type="javax.sql.DataSource"
    driverClassName="org.mariadb.jdbc.Driver"
    url="jdbc:mariadb://localhost:3306/musicdb"
    username="prospring6"
    password="prospring6"
    maxTotal="20"
    maxIdle="10"
    maxWaitMillis="-1"/>
}
 */
@Configuration
@PropertySource("classpath:jndi/jndi.properties")
public class JndiDataSourceCfg {
	
    private static Logger LOGGER = LoggerFactory.getLogger(JndiDataSourceCfg.class);
    
	@Value("${jndi.dataSourceName}")
	private String jndiDataSourceName;

    @Bean
    public DataSource dataSource() {
        try {
        	return (DataSource) new JndiTemplate().lookup(jndiDataSourceName);
        } catch (Exception e) {
            LOGGER.error("JNDI DataSource bean cannot be created!", e);
            return null;
        }
    }
}
