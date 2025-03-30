package it.maxmin.dao.jpa.integration.step.config;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = { JpaDaoSpringContextIntegrationTestCfg.class })
@TestPropertySource("classpath:jndi/jndi-integration.properties")
public class StepCfg {

}
