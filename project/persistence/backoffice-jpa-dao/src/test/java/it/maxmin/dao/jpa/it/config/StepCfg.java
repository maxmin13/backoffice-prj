package it.maxmin.dao.jpa.it.config;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;
import it.maxmin.dao.jpa.config.JpaDaoIntegrationTestCfg;

@CucumberContextConfiguration
@ContextConfiguration(classes = { JpaDaoIntegrationTestCfg.class })
@TestPropertySource("classpath:jndi/jndi-integration.properties")
public class StepCfg {

}
