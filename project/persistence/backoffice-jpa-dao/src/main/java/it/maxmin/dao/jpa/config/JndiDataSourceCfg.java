package it.maxmin.dao.jpa.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

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
@PropertySource("classpath:jndi/jndi.properties")
public class JndiDataSourceCfg {

	@Value("${jndi.dataSourceName}")
	private String jndiDataSourceName;

	@Bean
	public DataSource dataSource() {
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		dsLookup.setResourceRef(true);
		return dsLookup.getDataSource(jndiDataSourceName);
	}

}
