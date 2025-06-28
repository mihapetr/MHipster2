package com.mihael.mhipster.cucumber;

import static io.cucumber.junit.platform.engine.Constants.*;

import com.mihael.mhipster.IntegrationTest;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/feature_testing.feature") // Path to feature files
//@SelectClasspathResource("com/mihael/mhipster/cucumber/user.feature") // Path to feature files
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.mihael.mhipster.cucumber, com.mihael.mhipster.cucumber.stepdefs")
class CucumberIT {}
