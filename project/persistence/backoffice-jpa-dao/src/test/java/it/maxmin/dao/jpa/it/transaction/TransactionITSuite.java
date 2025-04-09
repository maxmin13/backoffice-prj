package it.maxmin.dao.jpa.it.transaction;

import static io.cucumber.core.options.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/transaction")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "it.maxmin.dao.jpa.it")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@transaction and not @ignore")
@ConfigurationParameter(key = "cucumber.execution.parallel.enabled", value = "false")
@ConfigurationParameter(key = "cucumber.execution.parallel.config.strategy", value = "fixed")
@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.max-pool-size", value = "4")
@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.parallelism", value = "4")
public class TransactionITSuite {

}
