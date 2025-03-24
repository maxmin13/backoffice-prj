package it.maxmin.dao.jpa.integration.step.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.util.LogUtil;
import it.maxmin.dao.jpa.integration.step.util.TransactionUtil;
import it.maxmin.model.jpa.dao.entity.User;

public class CleanUserScenarioHooks {

	private ScenarioContext scenarioContext;
	private TransactionUtil transactionUtil;
	private LogUtil logUtil;
	private UserDao userDao;

	@Autowired
	public CleanUserScenarioHooks(ScenarioContext scenarioContext, UserDao userDao, TransactionUtil transactionUtil,
			LogUtil logUtil) {
		this.scenarioContext = scenarioContext;
		this.userDao = userDao;
		this.transactionUtil = transactionUtil;
		this.logUtil = logUtil;
	}

	@After("@deleteUsers")
	public void cleanScenarioContext(Scenario scenario) {
		logUtil.log("getting scenario {0} context", scenarioContext.getScenarioName());
		scenarioContext.getSavedObjects().stream().forEach(obj -> {
			if (obj instanceof User) {
				User user = (User) obj;
				TransactionDefinition transactionDefinition = transactionUtil.createTx(scenarioContext.getScenarioName());
				TransactionStatus transaction = transactionUtil.startTx(transactionDefinition);
				userDao.findByAccountName(user.getAccountName()).ifPresentOrElse(u -> {
					logUtil.log("deleting user {0}", u.getAccountName());
					userDao.deleteByAccountName(u.getAccountName());
					transactionUtil.commitTx(transaction);
					logUtil.log("user {0} deleted", u.getAccountName());
				}, () -> transactionUtil.rollbackTx(transaction));
			}
		});
		logUtil.log("scenario {0} context cleaned", scenarioContext.getScenarioName());
	}

}
