package it.maxmin.dao.jpa.integration.step.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;

@Configuration
@Import(JpaDaoSpringContextCfg.class)
public class JpaDaoSpringContextIntegrationTestCfg {

	@Bean
	public StepUtil steUtil() {
		return new StepUtil();
	}
}
