package com.mihael.mhipster.cucumber.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.boot.test.web.server.LocalServerPort;

public class Feature_testingStepDefs extends Common {

    static String CWD;

    @Given("user is positioned in root directory in local project files")
    public void user_is_positioned_in_m_hipster_directory_in_local_project_files() {
        CWD = "/home/mihael/Projects/MHipster2/";
    }

    @When("user runs generate step definitions script")
    public void user_runs_generate_step_definitions_script() {}

    @Then("step definition stubs are created")
    public void step_definition_stubs_are_created() {}

    @Given("user specifies which features to test")
    public void user_specifies_which_features_to_test() {}

    @When("user runs test script")
    public void user_runs_test_script() throws IOException, InterruptedException {
        String[] command = { CWD + "test_features.sh", String.valueOf(port), "2", "user", "user" };
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // capture output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
    }

    @Then("integration testing based on feature files should start")
    public void integration_testing_based_on_feature_files_should_start() {}

    @Given("automatic feature testing was executed")
    public void automatic_feature_testing_was_executed() {}

    @When("all selected feature tests have passed")
    public void all_selected_feature_tests_have_passed() {}

    @Transactional
    @Then("test result is posted to the platform")
    public void test_result_is_posted_to_the_platform() {}
}
