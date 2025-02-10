package it.maxmin.dao.jpa.step.user;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/user")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "it.maxmin.dao.jpa.step")
//@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @Ignore")
//@ConfigurationParameter(key = "cucumber.execution.parallel.enabled", value = "true")
//@ConfigurationParameter(key = "cucumber.execution.parallel.config.strategy", value = "fixed")
//@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.max-pool-size", value = "3")
//@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.parallelism", value = "2")
public class UserITRunner {

}
