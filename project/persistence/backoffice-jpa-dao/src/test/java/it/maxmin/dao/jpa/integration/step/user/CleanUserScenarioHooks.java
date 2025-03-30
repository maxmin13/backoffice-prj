package it.maxmin.dao.jpa.integration.step.user;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.integration.step.common.LogUtil;
import it.maxmin.dao.jpa.integration.step.common.StepActionManager;
import it.maxmin.dao.jpa.integration.step.transaction.StepTransactionManager;
import it.maxmin.model.jpa.dao.entity.User;

public class CleanUserScenarioHooks {

	private StepTransactionManager stepTransactionManager;
	private StepActionManager stepActionManager;
	private LogUtil logUtil;
	private UserDao userDao;

	@Autowired
	public CleanUserScenarioHooks(StepTransactionManager stepTransactionManager, StepActionManager stepActionManager,
			LogUtil logUtil, UserDao userDao) {
		this.stepTransactionManager = stepTransactionManager;
		this.stepActionManager = stepActionManager;
		this.logUtil = logUtil;
		this.userDao = userDao;
	}

	@After("@deleteUsers")
	public void cleanScenarioContext(Scenario scenario) {
		stepActionManager.getItemsOfType(User.class).stream().forEach(user -> {
			String id = stepTransactionManager.createTx();
			stepTransactionManager.startTx(id);
			userDao.findByAccountName(user.getAccountName()).ifPresentOrElse(u -> {
				logUtil.log("deleting user {0}", u.getAccountName());
				userDao.deleteByAccountName(u.getAccountName());
				stepTransactionManager.commitTx(id);
				logUtil.log("user {0} deleted", u.getAccountName());
			}, () -> stepTransactionManager.rollbackTx(id));
		});
	}

}
