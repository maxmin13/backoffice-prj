package it.maxmin.dao.jpa.integration.step.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;
import it.maxmin.dao.jpa.integration.step.common.LogUtil;
import it.maxmin.dao.jpa.integration.step.common.StepActionManager;
import it.maxmin.dao.jpa.integration.step.common.StepErrorHelper;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.transaction.StepTransactionHelper;
import it.maxmin.dao.jpa.integration.step.transaction.StepTransactionManager;

@Import({ JpaDaoSpringContextCfg.class, ScenarioContext.class })
public class JpaDaoSpringContextIntegrationTestCfg {

	@DependsOn("logUtil")
	@Bean("stepTransactionManager")
	public StepTransactionManager stepTransactionManager(PlatformTransactionManager platformTransactionManager,
			ScenarioContext scenarioContext, MessageService messageService, LogUtil logUtil) {
		return new StepTransactionManager(scenarioContext, platformTransactionManager, messageService, logUtil);
	}

	@DependsOn("logUtil")
	@Bean("stepActionManager")
	public StepActionManager stepActionManager(ScenarioContext scenarioContext, MessageService messageService) {
		return new StepActionManager(scenarioContext, messageService);
	}

	@Bean("stepTransactionHelper")
	public StepTransactionHelper stepTransactionHelper() {
		return new StepTransactionHelper();
	}

	@Bean("stepErrorHelper")
	public StepErrorHelper stepErrorHelper() {
		return new StepErrorHelper();
	}

	@Bean("logUtil")
	public LogUtil logUtil(ScenarioContext scenarioContext) {
		return new LogUtil(scenarioContext);
	}

}
