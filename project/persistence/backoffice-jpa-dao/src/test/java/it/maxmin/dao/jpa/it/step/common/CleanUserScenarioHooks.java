package it.maxmin.dao.jpa.it.step.common;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.it.step.context.ScenarioActionContext;
import it.maxmin.dao.jpa.it.step.transaction.StepTransactionManager;
import it.maxmin.model.jpa.dao.entity.User;

public class CleanUserScenarioHooks {

	private StepTransactionManager stepTransactionManager;
	private ScenarioActionContext scenarioActionContext;
	private LogUtil logUtil;
	private UserDao userDao;

	@Autowired
	public CleanUserScenarioHooks(StepTransactionManager stepTransactionManager,
			ScenarioActionContext scenarioActionContext, LogUtil logUtil, UserDao userDao) {
		this.stepTransactionManager = stepTransactionManager;
		this.scenarioActionContext = scenarioActionContext;
		this.logUtil = logUtil;
		this.userDao = userDao;
	}

	@After("@deleteUsers")
	public void cleanScenarioContext(Scenario scenario) {
		scenarioActionContext.getItemsOfType(User.class).stream().forEach(user -> {
			String name = stepTransactionManager.createTx();
			stepTransactionManager.startTx(name);
			userDao.findByAccountName(user.getAccountName()).ifPresentOrElse(u -> {
				userDao.delete(u);
				stepTransactionManager.commitTx(name);
				logUtil.log("Deleted user {0} ", u);
			}, () -> stepTransactionManager.rollbackTx(name));
		});
	}

}
