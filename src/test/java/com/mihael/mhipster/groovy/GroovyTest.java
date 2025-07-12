package com.mihael.mhipster.groovy;

import com.mihael.mhipster.AnnotationUtil;
import com.mihael.mhipster.MDLSProcessor;
import com.mihael.mhipster.ReportParser;
import com.mihael.mhipster.StepdefGenerator;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.domain.TestReport;
import com.mihael.mhipster.domain.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class GroovyTest {

    @Test
    void MDLSProcessorTransformTest() throws IOException {
        MDLSProcessor.transform(
            Files.readString(
                Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/jdl-template.jdl"),
                StandardCharsets.UTF_8
            ),
            Files.readString(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/mdls.jdl"), StandardCharsets.UTF_8),
            "appName",
            "com.test.test",
            "/home/mihael/Projects/unitTestDestination/specification.jdl"
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
    void reportParserTest() throws IOException {
        TestReport testReport = ReportParser.html2TestReport(
            new String(
                Files.readAllBytes(Path.of("/home/mihael/Projects/MHipster2/target/site/jacoco-mit-runtime/index.html")),
                StandardCharsets.UTF_8
            )
        );
        System.out.println(testReport);
    }

    @Test
    public void user_runs_test_script() throws IOException, InterruptedException {
        String CWD = "/home/mihael/Projects/MHipster2";
        Process process = Runtime.getRuntime().exec(new String[] { CWD + "/test_features.sh", "user", "user" });
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Exited with code: " + exitCode);
    }

    String resources = "/home/mihael/Projects/MHipster2/src/main/resources/mhipster/";

    @Test
    void testProjectGen() {
        String projectRoot = System.getProperty("user.dir");
        String parentDir = new File(projectRoot).getParent();
        System.out.println(parentDir);

        User user = new User();
        user.setLogin("mihael");

        Project project = new Project();
        project.setName("testProjectName");

        project.setUser(user);
    }
}
