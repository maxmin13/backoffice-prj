package it.maxmin.dao.jpa.integration.step.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;

@Import(JpaDaoSpringContextCfg.class)
public class JpaDaoSpringContextIntegrationTestCfg {

	@Bean
	public StepUtil stepUtil() {
		return new StepUtil();
	}
}
