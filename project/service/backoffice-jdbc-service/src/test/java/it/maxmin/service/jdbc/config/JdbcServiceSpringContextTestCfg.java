package it.maxmin.service.jdbc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.maxmin.dao.jdbc.config.JdbcDaoSpringContextTestCfg;

@Configuration
@Import({ JdbcServiceSpringContextCfg.class,JdbcDaoSpringContextTestCfg.class })
public class JdbcServiceSpringContextTestCfg {

}
