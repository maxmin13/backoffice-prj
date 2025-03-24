package it.maxmin.dao.jpa.integration.step.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.util.LogUtil;
import it.maxmin.dao.jpa.integration.step.util.StepErrorUtil;
import it.maxmin.dao.jpa.integration.step.util.TransactionUtil;

@Import(JpaDaoSpringContextCfg.class)
public class JpaDaoSpringContextIntegrationTestCfg {

	@DependsOn("logUtil")
	@Bean
	public TransactionUtil transactionUtil(PlatformTransactionManager platformTransactionManager, LogUtil logUtil) {
		return new TransactionUtil(platformTransactionManager, logUtil);
	}

	@Bean
	public StepErrorUtil stepErrorUtil() {
		return new StepErrorUtil();
	}

	@Bean("logUtil")
	public LogUtil logUtil(ScenarioContext scenarioContext) {
		return new LogUtil(scenarioContext);
	}

}
