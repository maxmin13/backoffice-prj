package it.maxmin.dao.jpa.step;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;
import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;
import it.maxmin.dao.jpa.step.common.StepUtil;

@CucumberContextConfiguration
@ContextConfiguration(classes = { JpaDaoSpringContextCfg.class, StepUtil.class })
@TestPropertySource("classpath:jndi/jndi-integration.properties")
public class BaseDatabaseStep {

}
