package it.maxmin.dao.jpa.integration.step.common.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jpa.integration.step.common.ScenarioContext;

public class LogUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
	
	private ScenarioContext scenarioContext;
	
	@Autowired
	public LogUtil(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	public void log(String message, Object... params) {
		StringBuffer bf = new StringBuffer();
		bf.append("SCE(").append(scenarioContext.getScenarioName()).append("): ").append(message);
		LOGGER.debug(MessageFormat.format(bf.toString(), params));
	}
}
