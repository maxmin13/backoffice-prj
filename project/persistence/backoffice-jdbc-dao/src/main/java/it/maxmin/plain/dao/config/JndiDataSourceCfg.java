package it.maxmin.plain.dao.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;

/**
 * Assume Datasource is configured as a resource in an application such as
 * Apache Tomcat {@code
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
@Import(JndiCfg.class)
@PropertySource("classpath:jndi/jndi.properties")
public class JndiDataSourceCfg {

	private static final Logger LOGGER = LoggerFactory.getLogger(JndiDataSourceCfg.class);

	@Value("${jndi.dataSourceName}")
	private String jndiDataSourceName;

	private JndiTemplate jndiTemplate;

	@Autowired
	public JndiDataSourceCfg(JndiTemplate jndiTemplate) {
		this.jndiTemplate = jndiTemplate;
	}

	@Bean
	public DataSource dataSource() {
		try {
			return (DataSource) jndiTemplate.lookup(jndiDataSourceName);
		}
		catch (Exception e) {
			LOGGER.error("JNDI DataSource bean cannot be created!", e);
			return null;
		}
	}

 }
