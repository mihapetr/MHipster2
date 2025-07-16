package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.domain.*;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

public class Feature_testingStepDefs extends Common {

    static String CWD;

    static boolean done = false;
    User user;
    static Long projectId;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ProjectRepository projectRepository;

    InputStream featureContentStream;

    @Before
    public void before() throws IOException {
        user = userRepository.findOneByLogin(existingLogin).orElseThrow();

        if (!done) {
            done = true;
            setup();

            // for project generation scenario
            featureContentStream = getClass().getClassLoader().getResourceAsStream("mhipster/personal_projects_overview.feature");

            Feature feature = new Feature();
            feature.setName("featureName");
            feature.setContent(
                //Files.readString(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/personal_projects_overview.feature"), StandardCharsets.UTF_8)
                new String(featureContentStream.readAllBytes(), StandardCharsets.UTF_8)
            );
            feature.setUser(user);

            featureRepository.save(feature);

            Project project = new Project().user(user).name("Test project name");
            project.addFeature(feature);
            projectId = projectRepository.save(project).getId();
            System.out.println("projectId: " + projectId);
        }
    }

    @Given("user is positioned in root directory in local project files")
    public void user_is_positioned_in_m_hipster_directory_in_local_project_files() {
        CWD = "/home/mihael/Projects/MHipster2/";
    }

    @Given("user specifies which features to test")
    public void user_specifies_which_features_to_test() {}

    @When("user runs test script")
    public void user_runs_test_script() throws IOException, InterruptedException {
        //        String[] command = { CWD + "test_features.sh", String.valueOf(port), "2", "user", "user" };
        //        ProcessBuilder pb = new ProcessBuilder(command);
        //        pb.redirectErrorStream(true);
        //        Process process = pb.start();
        //
        //        // capture output
        //        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        //        String line;
        //        while ((line = reader.readLine()) != null) {
        //            System.out.println(line);
        //        }
        //        int exitCode = process.waitFor();
    }

    @Then("integration testing based on feature files should start")
    public void integration_testing_based_on_feature_files_should_start() {}

    @Given("automatic feature testing was executed")
    public void automatic_feature_testing_was_executed() {}

    @When("all selected feature tests have passed")
    public void all_selected_feature_tests_have_passed() {}

    @Then("test result is posted to the platform")
    public void test_result_is_posted_to_the_platform() throws IOException {
        token = signIn(existingLogin, existingPassword);

        res = restClient
            .post()
            .uri("http://localhost:" + port + "/api/test-reports/of-project/" + projectId)
            .contentType(MediaType.TEXT_HTML)
            .header("Authorization", "Bearer " + token)
            .body(Files.readAllBytes(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/index.html")))
            .retrieve();
        res.toEntity(FeatureTst.class);
        Long featureTstId = res.toEntity(TestReport.class).getBody().getFeatureTst().getId();

        res = restClient
            .post()
            .uri("http://localhost:" + port + "/api/test-reports/of-feature-test/" + featureTstId)
            .contentType(MediaType.TEXT_HTML)
            .header("Authorization", "Bearer " + token)
            .body(Files.readAllBytes(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/index.html")))
            .retrieve();

        System.out.println(res.toEntity(TestReport.class));

        res = restClient
            .get()
            .uri("http://localhost:" + port + "/api/feature-tsts/" + featureTstId)
            .header("Authorization", "Bearer " + token)
            .retrieve();

        System.out.println(res.toEntity(FeatureTst.class));
    }
}
