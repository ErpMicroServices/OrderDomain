package org.erp_microservices.peopleandorganizations.bdd;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "classpath:features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "org.erp_microservices.peopleandorganizations.bdd")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@bdd")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:build/reports/tests/bdd/cucumber.html")
public class BddTestRunner {
}