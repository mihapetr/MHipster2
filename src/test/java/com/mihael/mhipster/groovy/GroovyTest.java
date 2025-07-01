package com.mihael.mhipster.groovy;

import com.mihael.mhipster.AnnotationUtil;
import com.mihael.mhipster.MDLSProcessor;
import com.mihael.mhipster.ReportParser;
import com.mihael.mhipster.StepdefGenerator;
import org.junit.jupiter.api.Test;

public class GroovyTest {

    @Test
    void MDLSProcessorTransformTest() {
        MDLSProcessor.transform(
            "/home/mihael/Projects/MHipster2/src/main/resources/mhipster/jdl-template.jdl",
            "/home/mihael/Projects/MHipster2/src/main/resources/mhipster/mdls.jdl",
            "appName",
            "com.test.test"
        );
    }

    @Test
    void MDLSProcessorDomainTest() {
        MDLSProcessor.modifyDomain(
            "/home/mihael/Projects/MHipster2/src/main/resources/mhipster/complete.jdl",
            "/home/mihael/Projects/MHipster2/src/main/java/com/mihael/mhipster/fakedomain"
        );
    }

    @Test
    void AnnotationUtilTest() {}

    @Test
    void stepdefGeneratorTest() {
        StepdefGenerator.generateStepdefs(
            "/home/mihael/Projects/MHipster2/src/main/resources/mhipster/personal_projects_overview.feature",
            "/home/mihael/Projects/MHipster2/src/main/resources/mhipster",
            "my.pack.name"
        );
    }

    @Test
    void reportParserTest() {
        ReportParser.extractToJson(
            "/home/mihael/Projects/MHipster2/target/site/jacoco-mit-runtime/index.html",
            "/home/mihael/Projects/MHipster2/src/main/resources/mhipster"
        );
    }
}
