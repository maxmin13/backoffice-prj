package it.maxmin.dao.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.common.StepErrorHelper;
import it.maxmin.dao.jpa.it.context.ScenarioActionContext;
import it.maxmin.dao.jpa.it.context.ScenarioContext;
import it.maxmin.dao.jpa.it.context.ScenarioTransactionContext;
import it.maxmin.dao.jpa.it.transaction.StepTransactionHelper;
import it.maxmin.dao.jpa.it.transaction.StepTransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionManager;

@Import({ JpaDaoSpringContextCfg.class, ScenarioContext.class, ScenarioActionContext.class,
		ScenarioTransactionContext.class })
public class JpaDaoSpringContextIntegrationTestCfg {

	@Bean("transactionManager")
	public TransactionManager transactionManager(PlatformTransactionManager platformTransactionManager,
			LogScenarioUtil logScenarioUtil) {
		return new TransactionManager(platformTransactionManager, logScenarioUtil);
	}

	@DependsOn("logScenarioUtil")
	@Bean("stepTransactionManager")
	public StepTransactionManager stepTransactionManager(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, MessageService messageService, LogScenarioUtil logScenarioUtil) {
		return new StepTransactionManager(transactionManager, scenarioTransactionContext, messageService, logScenarioUtil);
	}

	@Bean("stepTransactionHelper")
	public StepTransactionHelper stepTransactionHelper() {
		return new StepTransactionHelper();
	}

	@Bean("stepErrorHelper")
	public StepErrorHelper stepErrorHelper() {
		return new StepErrorHelper();
	}

	@Bean("logScenarioUtil")
	public LogScenarioUtil logScenarioUtil(ScenarioContext scenarioContext) {
		return new LogScenarioUtil(scenarioContext);
	}

}
