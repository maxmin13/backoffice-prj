package it.maxmin.dao.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.it.common.FeatureErrorHelper;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioContext;
import it.maxmin.dao.jpa.it.context.ScenarioErrorContext;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.dao.jpa.it.context.ScenarioTransactionContext;
import it.maxmin.dao.jpa.it.context.StepErrorManager;
import it.maxmin.dao.jpa.it.context.StepTransactionManager;
import it.maxmin.dao.jpa.it.transaction.FeatureTransactionHelper;
import it.maxmin.dao.jpa.transaction.TransactionManager;

@Import({ JpaDaoSpringContextCfg.class, ScenarioContext.class, ScenarioItemContext.class,
		ScenarioTransactionContext.class, ScenarioErrorContext.class })
public class JpaDaoSpringContextIntegrationTestCfg {

	@Bean("stepErrorManager")
	public StepErrorManager stepErrorManager(ScenarioErrorContext scenarioErrorContext, MessageService messageService) {
		return new StepErrorManager(scenarioErrorContext, messageService);
	}

	@Bean("transactionManager")
	public TransactionManager transactionManager(PlatformTransactionManager platformTransactionManager,
			LogScenarioUtil logScenarioUtil) {
		return new TransactionManager(platformTransactionManager, logScenarioUtil);
	}

	@DependsOn("logScenarioUtil")
	@Bean("stepTransactionManager")
	public StepTransactionManager stepTransactionManager(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, MessageService messageService) {
		return new StepTransactionManager(transactionManager, scenarioTransactionContext, messageService);
	}

	@Bean("stepTransactionHelper")
	public FeatureTransactionHelper stepTransactionHelper() {
		return new FeatureTransactionHelper();
	}

	@Bean("stepErrorHelper")
	public FeatureErrorHelper stepErrorHelper() {
		return new FeatureErrorHelper();
	}

	@Bean("logScenarioUtil")
	public LogScenarioUtil logScenarioUtil(ScenarioContext scenarioContext) {
		return new LogScenarioUtil(scenarioContext);
	}

}
