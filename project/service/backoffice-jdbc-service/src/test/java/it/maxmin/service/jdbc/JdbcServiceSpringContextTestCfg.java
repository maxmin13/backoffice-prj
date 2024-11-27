package it.maxmin.service.jdbc;

import org.springframework.context.annotation.Configuration;
import it.maxmin.dao.jdbc.JdbcDaoSpringContextTestCfg;
import org.springframework.context.annotation.Import;

import it.maxmin.service.jdbc.config.JdbcServiceSpringContextCfg;

@Configuration
@Import({ JdbcServiceSpringContextCfg.class,JdbcDaoSpringContextTestCfg.class })
public class JdbcServiceSpringContextTestCfg {

}
