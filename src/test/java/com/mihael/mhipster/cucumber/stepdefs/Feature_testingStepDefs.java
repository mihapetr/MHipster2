package com.mihael.mhipster.cucumber.stepdefs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.domain.*;
import com.mihael.mhipster.repository.CodeStatsRepository;
import com.mihael.mhipster.repository.FeatureTstRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import com.mihael.mhipster.service.dto.AdminUserDTO;
import com.mihael.mhipster.service.mapper.UserMapper;
import com.mihael.mhipster.web.rest.CodeStatsResource;
import com.mihael.mhipster.web.rest.FeatureTstResource;
import com.mihael.mhipster.web.rest.ProjectResource;
import com.mihael.mhipster.web.rest.TestReportResource;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class Feature_testingStepDefs {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    ProjectResource projectResource;

    @Autowired
    FeatureTstResource featureTstResource;

    @Autowired
    CodeStatsResource codeStatsResource;

    @Autowired
    TestReportResource testReportResource;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CodeStatsRepository codeStatsRepository;

    @Autowired
    FeatureTstRepository featureTstRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    static Long projectId;
    static int SCENARIO_NUMBER = 3;
    static boolean requiresSetup = true;

    // Find a user with username admin and create a project whose owner is that user. Save the project's id for later.
    @Before
    public void setup() throws Exception {
        // perform this only before the first scenario
        if (!requiresSetup) return;
        requiresSetup = false;

        MvcResult userResult = mockMvc.perform(get("/api/admin/users/admin")).andExpect(status().isOk()).andReturn();
        System.out.println("users: " + userResult.getResponse().getContentAsString());

        AdminUserDTO userDTO = objectMapper.readValue(userResult.getResponse().getContentAsString(), AdminUserDTO.class);
        User user = userMapper.userDTOToUser(userDTO);

        Project project = new Project().name("test_name").user(user);
        String project_JSON = objectMapper.writeValueAsString(project);

        MvcResult result = mockMvc
            .perform(post("/api/projects").contentType(MediaType.APPLICATION_JSON).content(project_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Project created = objectMapper.readValue(responseBody, Project.class);
        projectId = created.getId();
    }

    @After
    public void tearDown() throws Exception {
        // delete only after the last scenario
        SCENARIO_NUMBER--; // counter to know when the last scenario is executed
        if (SCENARIO_NUMBER > 0) return;

        projectRepository.deleteAll();
        codeStatsRepository.deleteAll();
        featureTstRepository.deleteAll();
    }

    @Given("user is positioned in MHipster directory in local project files")
    public void user_is_positioned_in_m_hipster_directory_in_local_project_files() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @When("user runs generate step definitions script")
    public void user_runs_generate_step_definitions_script() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @Then("step definition stubs are created")
    public void step_definition_stubs_are_created() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @Given("user specifies which features to test")
    public void user_specifies_which_features_to_test() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @When("user runs test script")
    public void user_runs_test_script() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @Then("integration testing based on feature files should start")
    public void integration_testing_based_on_feature_files_should_start() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @Given("automatic feature testing was executed")
    public void automatic_feature_testing_was_executed() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @When("all selected feature tests have passed")
    public void all_selected_feature_tests_have_passed() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @Transactional
    @Then("test result is posted to the platform")
    public void test_result_is_posted_to_the_platform() throws Exception {
        FeatureTst featureTst = new FeatureTst();

        TestReport testReport = new TestReport().featureTst(featureTst).runtimeRetention(false);
        String testReport_JSON = objectMapper.writeValueAsString(testReport);
        MvcResult result = mockMvc
            .perform(post("/api/test-reports/of-project/" + projectId).contentType(MediaType.APPLICATION_JSON).content(testReport_JSON))
            .andExpect(status().isCreated())
            .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        /*result = mockMvc.perform(
                get("/api/projects/" + projectId)
            )
            //.andExpect(status().isOk())
            .andReturn();
        System.out.println("whole project = " + result.getResponse().getContentAsString());

        Project project = projectResource.getProject(projectId).getBody();
        System.out.println("project id = " + project.getId());
        System.out.println("project user = " + project.getUser().getLogin());

        FeatureTst featureTst1 = featureTstResource.getFeatureTst(projectId).getBody();

        System.out.println("featureTst project id = " + featureTst1.getProject().getId());*/

        //List<Project> projects =
        //for (Project project : projects) {project.getFeatureTsts().size();}

        //List<FeatureTst> featureTsts =
        //for (FeatureTst fts : featureTsts) {fts.getTestReports().size();}

        testEntityManager.flush();
        testEntityManager.clear();

        List<TestReport> testReports = testReportResource.getAllTestReports();

        System.out.println("projects = " + projectRepository.findAllWithEagerRelationships());
        System.out.println("featureTsts = " + featureTstRepository.findAllWithEagerRelationships());
        System.out.println("testReports = " + testReports);
    }
}
