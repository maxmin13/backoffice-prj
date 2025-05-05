package it.maxmin.dao.jpa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({ JpaDaoSpringContextCfg.class })
@ComponentScan(basePackages = { "it.maxmin.dao.jpa.transaction", "it.maxmin.dao.jpa.it" })
public class JpaDaoIntegrationTestCfg {

}
