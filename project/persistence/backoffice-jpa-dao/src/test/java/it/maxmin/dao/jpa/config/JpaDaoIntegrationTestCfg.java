package it.maxmin.dao.jpa.config;

import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import it.maxmin.dao.jpa.transaction.TransactionProperty;

@Import({ JpaDaoSpringContextCfg.class })
@ComponentScan(basePackages = { "it.maxmin.dao.jpa.transaction", "it.maxmin.dao.jpa.it" })
public class JpaDaoIntegrationTestCfg {

	@Bean
	public TransactionProperty object() throws IOException {
		final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		return mapper.readValue(new File("src/test/resources/application-test.yaml"), TransactionProperty.class);
	}
}
