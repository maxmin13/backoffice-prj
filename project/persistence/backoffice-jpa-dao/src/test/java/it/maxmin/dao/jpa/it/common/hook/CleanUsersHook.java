package it.maxmin.dao.jpa.it.common.hook;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.dao.jpa.transaction.Transaction;
import it.maxmin.dao.jpa.transaction.TransactionManager;
import it.maxmin.model.jpa.dao.entity.User;

public class CleanUsersHook {

	private TransactionManager transactionManager;
	private ScenarioItemContext scenarioItemContext;
	private LogScenarioUtil logScenarioUtil;
	private UserDao userDao;

	@Autowired
	public CleanUsersHook(TransactionManager transactionManager, ScenarioItemContext scenarioItemContext,
			LogScenarioUtil logScenarioUtil, UserDao userDao) {
		this.transactionManager = transactionManager;
		this.scenarioItemContext = scenarioItemContext;
		this.logScenarioUtil = logScenarioUtil;
		this.userDao = userDao;
	}

	@After(order = 1, value = "@deleteUsers")
	public void clean(Scenario scenario) {
		scenarioItemContext.getItemsOfType(User.class).stream().forEach(user -> {
			Transaction transaction = transactionManager.createTx();
			transactionManager.startTx(transaction);
			userDao.findByAccountName(user.getAccountName()).ifPresentOrElse(u -> {
				userDao.delete(u);
				transactionManager.commitTx(transaction);
				logScenarioUtil.log("Deleted user {0} ", u);
			}, () -> transactionManager.rollbackTx(transaction));
		});
	}

}
