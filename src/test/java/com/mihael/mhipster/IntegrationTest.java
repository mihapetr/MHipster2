package com.mihael.mhipster;

import com.mihael.mhipster.config.AsyncSyncConfiguration;
import com.mihael.mhipster.config.EmbeddedSQL;
import com.mihael.mhipster.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MHipsterApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@WithMockUser(username = "admin", roles = "ADMIN")
public @interface IntegrationTest {
}
