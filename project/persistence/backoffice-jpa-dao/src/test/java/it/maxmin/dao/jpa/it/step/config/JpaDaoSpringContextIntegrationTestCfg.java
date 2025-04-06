package it.maxmin.dao.jpa.it.step.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;
import it.maxmin.dao.jpa.it.step.common.LogUtil;
import it.maxmin.dao.jpa.it.step.common.StepErrorHelper;
import it.maxmin.dao.jpa.it.step.context.ScenarioActionContext;
import it.maxmin.dao.jpa.it.step.context.ScenarioContext;
import it.maxmin.dao.jpa.it.step.context.ScenarioTransactionContext;
import it.maxmin.dao.jpa.it.step.transaction.StepTransactionHelper;
import it.maxmin.dao.jpa.it.step.transaction.StepTransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionManager;

@Import({ JpaDaoSpringContextCfg.class, ScenarioContext.class, ScenarioActionContext.class,
		ScenarioTransactionContext.class })
public class JpaDaoSpringContextIntegrationTestCfg {

	@Bean("transactionManager")
	public TransactionManager transactionManager(PlatformTransactionManager platformTransactionManager,
			LogUtil logUtil) {
		return new TransactionManager(platformTransactionManager, logUtil);
	}

	@DependsOn("logUtil")
	@Bean("stepTransactionManager")
	public StepTransactionManager stepTransactionManager(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, MessageService messageService, LogUtil logUtil) {
		return new StepTransactionManager(transactionManager, scenarioTransactionContext, messageService, logUtil);
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
