package it.maxmin.service.jdbc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.maxmin.service.jdbc.config.JdbcServiceSpringContextCfg;

@Configuration
@Import({ JdbcServiceSpringContextCfg.class, it.maxmin.dao.jdbc.DatabaseUnitTestContextCfg.class })
public class DatabaseUnitTestContextCfg {

}
