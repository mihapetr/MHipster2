package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.domain.*;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.FeatureTstRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

public class Feature_testingStepDefs extends Common {

    static String CWD;

    static boolean done = false;
    User user;
    static String token;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    FeatureTstRepository featureTstRepository;

    @Autowired
    ProjectRepository projectRepository;

    static void execute(String directory, String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(directory));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // capture output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            //return process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Long projectId, feature1Id, feature2Id;

    /** Make sure that there is a Project with id specified in the
     * $CWD/mhipster/test_features.conf file. The project should have all features
     * from $CWD/feature_testing_selection.txt associated with it. Their id-s
     * should be the same as given in the file names __[feature_id]__*.feature.
     *
     * add FeatureTst patching to the script (FeatureTst -- Feature)
     * implement custom patching so that existing features are used for report (by ids)
     *
     */
    @Before
    public void before() throws IOException {
        user = userRepository.findOneByLogin(existingLogin).orElseThrow();

        if (!done) {
            token = signIn(existingLogin, existingPassword);

            done = true;
            setup();
            user = userRepository.findOneByLogin(existingLogin).orElseThrow();

            Feature feature = new Feature().name("Some feature name").content("Feature: Some feature name").user(user);

            feature = featureRepository.save(feature);

            Feature feature2 = new Feature().name("Some feature name 2").content("Feature: Some feature name 2").user(user);

            feature2 = featureRepository.save(feature2);

            Project project = new Project()
                .name("Some project name")
                .user(user)
                .description("Some description")
                .addFeature(feature)
                .addFeature(feature2);

            project = projectRepository.save(project);
            projectId = project.getId();
            feature1Id = feature.getId();
            feature2Id = feature2.getId();
        }
    }

    @Given("user is positioned in root directory in local project files")
    public void user_is_positioned_in_m_hipster_directory_in_local_project_files() {
        CWD = generatedProjectDirectory + "/downloaded/1151";
    }

    @Given("user specifies which features to test")
    public void user_specifies_which_features_to_test() {
        String featuresDir = CWD + "/src/test/resources/features";
        execute(featuresDir, "sh", "-c", "ls -1 > " + CWD + "/test_features_selection.txt");
    }

    @When("user runs test script")
    public void user_runs_test_script() throws IOException, InterruptedException {
        execute(CWD, "./test_features.sh", port + "", projectId + "", existingLogin, existingPassword, feature1Id + "," + feature2Id);
    }

    @Then("integration testing based on feature files should start")
    public void integration_testing_based_on_feature_files_should_start() {}

    @Given("automatic feature testing was executed")
    public void automatic_feature_testing_was_executed() {}

    @When("all selected feature tests have passed")
    public void all_selected_feature_tests_have_passed() {}

    @Then("test result is posted to the platform")
    public void test_result_is_posted_to_the_platform() throws IOException {
        List<FeatureTst> featureTsts = restClient
            .get()
            .uri("http://localhost:" + port + "/api/feature-tsts?filter=current-user")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<FeatureTst>>() {})
            .getBody();

        System.out.println(featureTsts);
        for (FeatureTst featureTst : featureTsts) {
            System.out.println(featureTst.getFeatures());
        }
    }
}
