package com.mihael.mhipster;

import com.mihael.mhipster.config.AsyncSyncConfiguration;
import com.mihael.mhipster.config.EmbeddedSQL;
import com.mihael.mhipster.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@SpringBootTest(classes = { MHipsterApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTestMock {
}
