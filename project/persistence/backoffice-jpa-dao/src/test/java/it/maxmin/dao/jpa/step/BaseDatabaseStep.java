package it.maxmin.dao.jpa.step;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;
import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;

@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
@CucumberContextConfiguration
@ContextConfiguration(classes = { JpaDaoSpringContextCfg.class, TestContext.class })
@TestPropertySource("classpath:jndi/jndi-integration.properties")
public class BaseDatabaseStep {

}
