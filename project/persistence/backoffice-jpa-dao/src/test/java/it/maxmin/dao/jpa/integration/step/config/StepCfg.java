package it.maxmin.dao.jpa.integration.step.config;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;

@CucumberContextConfiguration
@ContextConfiguration(classes = { JpaDaoSpringContextIntegrationTestCfg.class, ScenarioContext.class })
@TestPropertySource("classpath:jndi/jndi-integration.properties")
public class StepCfg {

}
