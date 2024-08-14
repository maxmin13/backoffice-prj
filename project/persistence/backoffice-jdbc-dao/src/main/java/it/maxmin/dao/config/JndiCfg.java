package it.maxmin.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.jndi.JndiTemplate;

public class JndiCfg {

	@Bean
	public JndiTemplate jndiTemplate() {
		return new JndiTemplate();
	}
}
