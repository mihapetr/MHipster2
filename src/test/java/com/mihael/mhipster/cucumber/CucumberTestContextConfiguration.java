package com.mihael.mhipster.cucumber;

import com.mihael.mhipster.IntegrationTest;
import com.mihael.mhipster.MHipsterApp;
import com.mihael.mhipster.config.AsyncSyncConfiguration;
import com.mihael.mhipster.config.EmbeddedSQL;
import com.mihael.mhipster.config.JacksonConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
public class CucumberTestContextConfiguration {}
