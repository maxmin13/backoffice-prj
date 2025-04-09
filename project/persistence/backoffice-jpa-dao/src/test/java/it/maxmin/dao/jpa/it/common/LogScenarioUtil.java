package it.maxmin.dao.jpa.it.common;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jpa.it.context.ScenarioContext;

public class LogScenarioUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogScenarioUtil.class);
	
	private ScenarioContext scenarioContext;
	
	@Autowired
	public LogScenarioUtil(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	public void log(String message, Object... params) {
		StringBuffer bf = new StringBuffer();
		bf.append("SCE(").append(scenarioContext.getScenarioName()).append("): ").append(message);
		LOGGER.debug(MessageFormat.format(bf.toString(), params));
	}
	
	public void error(String message, Object... params) {
		StringBuffer bf = new StringBuffer();
		bf.append("SCE(").append(scenarioContext.getScenarioName()).append("): ").append(message);
		LOGGER.error(MessageFormat.format(bf.toString(), params));
	}
}
