package it.maxmin.dao.jpa.integration.step.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import it.maxmin.common.service.api.MessageService;

public abstract class BaseStepDefinitions {

	private MessageService messageService;
	private StepContext stepContext;
	private StepUtil stepUtil;
	private PlatformTransactionManager transactionManager;

	@Autowired
	public BaseStepDefinitions(PlatformTransactionManager transactionManager, StepUtil stepUtil,
			MessageService messageService) {
		this.messageService = messageService;
		this.stepUtil = stepUtil;
		this.transactionManager = transactionManager;
		stepContext = new StepContext();
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public StepContext getStepContext() {
		return stepContext;
	}

	public StepUtil getStepUtil() {
		return stepUtil;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

}
