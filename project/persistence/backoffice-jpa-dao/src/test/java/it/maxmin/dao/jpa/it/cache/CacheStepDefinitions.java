package it.maxmin.dao.jpa.it.cache;

import io.cucumber.java.en.When;
import it.maxmin.dao.jpa.cache.JpaCacheManager;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;

public class CacheStepDefinitions {

	private JpaCacheManager jpaCacheManager;
	private LogScenarioUtil logScenarioUtil;

	public CacheStepDefinitions(JpaCacheManager jpaCacheManager, LogScenarioUtil logScenarioUtil) {
		this.jpaCacheManager = jpaCacheManager;
		this.logScenarioUtil = logScenarioUtil;
	}

	@When("I flush the application cache")
	public void delete_user() {
		jpaCacheManager.flush();
		logScenarioUtil.log("Hibernate primary cache flushed");
	}

}
