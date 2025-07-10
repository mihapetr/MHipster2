package com.mihael.mhipster.backup;

import com.mihael.mhipster.cucumber.stepdefs.Common;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;
import java.io.IOException;
import org.springframework.boot.test.web.server.LocalServerPort;

public class backup_Feature_testing_stepdefs {

    @LocalServerPort
    private int port;

    //    @Autowired
    //    private ObjectMapper objectMapper;
    //
    //    @Autowired
    //    private MockMvc mockMvc;
    //
    //    @Autowired
    //    private UserMapper userMapper;
    //
    //    @Autowired
    //    ProjectResource projectResource;
    //
    //    @Autowired
    //    FeatureTstResource featureTstResource;
    //
    //    @Autowired
    //    CodeStatsResource codeStatsResource;
    //
    //    @Autowired
    //    TestReportResource testReportResource;
    //
    //    @Autowired
    //    ProjectRepository projectRepository;
    //
    //    @Autowired
    //    CodeStatsRepository codeStatsRepository;
    //
    //    @Autowired
    //    FeatureTstRepository featureTstRepository;

    //    @Autowired
    //    private TestEntityManager testEntityManager;

    static Long projectId;
    static int SCENARIO_NUMBER = 3;
    static boolean requiresSetup = true;

    // Find a user with username admin and create a project whose owner is that user. Save the project's id for later.
    @Before
    public void setup() throws Exception {
        //        // perform this only before the first scenario
        //        if (!requiresSetup) return;
        //        requiresSetup = false;
        //
        //        MvcResult userResult = mockMvc.perform(get("/api/admin/users/admin")).andExpect(status().isOk()).andReturn();
        //        //System.out.println("users: " + userResult.getResponse().getContentAsString());
        //
        //        AdminUserDTO userDTO = objectMapper.readValue(userResult.getResponse().getContentAsString(), AdminUserDTO.class);
        //        User user = userMapper.userDTOToUser(userDTO);
        //
        //        Project project = new Project().name("test_name").user(user); // because mock user in tests is admin
        //        String project_JSON = objectMapper.writeValueAsString(project);
        //
        //        MvcResult result = mockMvc
        //            .perform(post("/api/projects").contentType(MediaType.APPLICATION_JSON).content(project_JSON))
        //            .andExpect(status().isCreated())
        //            .andReturn();
        //
        //        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        //        Project created = objectMapper.readValue(responseBody, Project.class);
        //        projectId = created.getId();
    }

    @After
    public void tearDown() throws Exception {
        //        // delete only after the last scenario
        //        SCENARIO_NUMBER--; // counter to know when the last scenario is executed
        //        if (SCENARIO_NUMBER > 0) return;
        //
        //        projectRepository.deleteAll();
        //        codeStatsRepository.deleteAll();
        //        featureTstRepository.deleteAll();
    }

    static String CWD;

    @Given("user is positioned in root directory in local project files")
    public void user_is_positioned_in_m_hipster_directory_in_local_project_files() {
        //CWD = "/home/mihael/Projects/MHipster2";

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
    public void user_runs_test_script() throws IOException, InterruptedException {
        //        Process process = Runtime.getRuntime().exec(new String[] { CWD + "/test_features.sh", "admin", "admin" });
        //        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        //        String line;
        //        while ((line = reader.readLine()) != null) {
        //            System.out.println(line);
        //        }
        //        int exitCode = process.waitFor();
        //        System.out.println("Exited with code: " + exitCode);
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
        //        TestReport testReportSource = new TestReport().runtimeRetention(false); // do not forget to sync with the retention modifier logic
        //        String testReportSource_JSON = objectMapper.writeValueAsString(testReportSource);
        //        // post the first report
        //        MvcResult result = mockMvc
        //            .perform(
        //                post("/api/test-reports/of-project/" + projectId).contentType(MediaType.APPLICATION_JSON).content(testReportSource_JSON)
        //            )
        //            .andExpect(status().isCreated())
        //            .andReturn();
        //        String result_JSON = result.getResponse().getContentAsString();
        //        //System.out.println("new report source = " + result_JSON);
        //        TestReport generatedReport = objectMapper.readValue(result_JSON, TestReport.class);
        //        Long generatedFeatureTstId = generatedReport.getFeatureTst().getId();
        //
        //        TestReport testReportRuntime = new TestReport().runtimeRetention(true).featureTst(generatedReport.getFeatureTst());
        //        String testReportRuntime_JSON = objectMapper.writeValueAsString(testReportRuntime);
        //        // post the second report
        //        result = mockMvc
        //            .perform(post("/api/test-reports").contentType(MediaType.APPLICATION_JSON).content(testReportRuntime_JSON))
        //            .andExpect(status().isCreated())
        //            .andReturn();
        //        result_JSON = result.getResponse().getContentAsString();
        //        //System.out.println("new report runtime = " + result_JSON);
        //
        //        // force the entity manager to look at DB for entity state before writing validation tests
        //        testEntityManager.flush();
        //        testEntityManager.clear();
        //
        //        //System.out.println("projects = " + projectRepository.findAll());
        //        FeatureTst featureTst = featureTstRepository.findOneWithEagerRelationships(generatedFeatureTstId).orElseThrow();
        //        //System.out.println("featureTsts = " + featureTst);
        //
        //        Set<Boolean> runtimes = featureTst.getTestReports().stream().map(TestReport::getRuntimeRetention).collect(Collectors.toSet());
        //
        //        assert runtimes.size() == 2 &&
        //        runtimes.contains(true) &&
        //        runtimes.contains(false) : "one report has to be runtime and the other has to be source, but was: " + runtimes;
        //
        //        assert featureTst.getProject().getId().equals(projectId) : "FeatureTst does not belong to the project from the setup";
    }
}
