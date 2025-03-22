package it.maxmin.dao.jpa.integration.step.user;

import org.springframework.transaction.PlatformTransactionManager;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.integration.step.common.AbstractStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.model.jpa.dao.entity.User;

public class CleanUserScenarioHooks extends AbstractStepDefinitions {

	private UserDao userDao;

	public CleanUserScenarioHooks(PlatformTransactionManager transactionManager, StepUtil stepUtil,
			MessageService messageService, ScenarioContext scenarioContext, UserDao userDao) {
		super(transactionManager, stepUtil, messageService, scenarioContext);
		this.userDao = userDao;
	}

	@After("@deleteUsers")
	public void cleanScenarioContext(Scenario scenario) {
		log("clean hook after scenario {0}", scenario.getId());
		log("getting scenario {0} context", getScenarioName());
		getSavedObjects().stream().forEach(o -> {
			if (o instanceof User) {
				User user = (User) o;
				startTx();
				userDao.findByAccountName(user.getAccountName()).ifPresentOrElse(u -> {
					log("deleting user {0}", u.getAccountName());
					userDao.deleteByAccountName(u.getAccountName());
					commitTx();
					log("user {0} deleted", u.getAccountName());
				}, () -> rollbackTx());
			}
		});
		log("scenario {0} context cleaned", getScenarioName());
	}

}
