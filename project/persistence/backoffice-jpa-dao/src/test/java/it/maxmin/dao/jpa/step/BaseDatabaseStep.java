package it.maxmin.dao.jpa.step;

import java.util.HashMap;
import java.util.Map;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.spring.CucumberContextConfiguration;
import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;


/////////////////////////////////////////// TODO
/// TRY TO CREATE A CommonDatabaseStep and move only there @@CucumberContextConfiguration
//////////////// @CucumberContextConfiguration
////////////////@ContextConfiguration(classes = { JpaDaoSpringContextCfg.class })
////////////////@TestPropertySource("classpath:jndi/jndi-integration.properties")


@CucumberContextConfiguration
@ContextConfiguration(classes = { JpaDaoSpringContextCfg.class })
@TestPropertySource("classpath:jndi/jndi-integration.properties")
public class BaseDatabaseStep {

	private static final Map<String, Exception> ERRORS = new HashMap<>(); ///////////// WHERE TO PUT IT???????
}
