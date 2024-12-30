package it.maxmin.service.jdbc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "it.maxmin.dao.jdbc.config", "it.maxmin.service.jdbc.impl", "it.maxmin.common.service.impl" })
public class JdbcServiceSpringContextCfg {

}
