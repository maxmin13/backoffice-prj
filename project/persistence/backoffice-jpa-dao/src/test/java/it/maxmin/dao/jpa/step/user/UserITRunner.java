package it.maxmin.dao.jpa.step.user;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "it.maxmin.dao.jpa.step")
//@ConfigurationParameter(key = "cucumber.execution.parallel.enabled", value = "true")
//@ConfigurationParameter(key = "cucumber.execution.parallel.config.strategy", value = "fixed")
//@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.max-pool-size", value = "3")
//@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.parallelism", value = "2")
public class UserITRunner {

}
