package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.MDLS;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.MDLSRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

public class Generating_projectsStepDefs extends Common {

    String url;
    User user, otherUser;
    Project projectToGenerate, projectToDownload;
    static Long projectToGenerateId, projectToDownloadId;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MDLSRepository mdlsRepository;

    InputStream featureContentStream, mdlsContentStream;

    static boolean done = false;

    @MGenerated
    @Before
    public void before() throws IOException {
        user = userRepository.findOneByLogin(existingLogin).orElse(null);
        otherUser = userRepository.findOneByLogin("user").orElseThrow();

        if (!done) {
            done = true;
            setup();
            user = userRepository.findOneByLogin(existingLogin).orElseThrow();
            Feature feature = new Feature().name("Other user's feature").content("Other Feature content").user(otherUser);
            MDLS mdls = new MDLS().content("Other user's MDLS content").user(otherUser);

            featureRepository.save(feature);
            mdlsRepository.save(mdls);

            // for project generation scenario
            featureContentStream = getClass().getClassLoader().getResourceAsStream("mhipster/personal_projects_overview.feature");
            mdlsContentStream = getClass().getClassLoader().getResourceAsStream("mhipster/mdls.jdl");

            Feature feature2 = new Feature();
            feature2.setName("featureName");
            feature2.setContent(
                //Files.readString(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/personal_projects_overview.feature"), StandardCharsets.UTF_8)
                new String(featureContentStream.readAllBytes(), StandardCharsets.UTF_8)
            );
            feature2.setUser(user);

            String mdlsContent = new String(mdlsContentStream.readAllBytes(), StandardCharsets.UTF_8);

            MDLS mdls2 = new MDLS();
            mdls2.setContent(
                //Files.readString(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/mdls.jdl"), StandardCharsets.UTF_8)
                mdlsContent
            );
            mdls2.setUser(user);

            MDLS mdls3 = new MDLS();
            mdls3.setContent(
                //Files.readString(Path.of("/home/mihael/Projects/MHipster2/src/main/resources/mhipster/mdls.jdl"), StandardCharsets.UTF_8)
                mdlsContent
            );
            mdls3.setUser(user);

            featureRepository.save(feature2);
            mdlsRepository.save(mdls2);
            mdlsRepository.save(mdls3);

            projectToGenerate = new Project().user(user).name("The one to generate").description("This one is for the gen files scenario");
            projectToGenerate.setMdls(mdls3); // one to one!
            projectToGenerate.addFeature(feature2);
            projectToGenerateId = projectRepository.save(projectToGenerate).getId();

            projectToDownload = new Project()
                .user(user)
                .name("The one to download")
                .location(generatedProjectDirectory + "/for-download/1151.zip")
                .description("This one is for the gen files scenario, too");
            projectToDownloadId = projectRepository.save(projectToDownload).getId();
        }
    }

    // will execute before every scenario because it is bcg step
    @Given("user is logged in : generating")
    public void user_is_logged_in_generating() {
        token = signIn(existingLogin, existingPassword);
    }

    @Given("user navigates to features view")
    public void user_navigates_to_features_view() {
        url = "/api/features";
    }

    @Given("user selects new feature option")
    public void user_selects_new_feature_option() {}

    Feature newFeatureForm;
    MDLS newMDLSForm;

    @Given("user fills the form")
    public void user_fills_the_form() {
        newFeatureForm = new Feature().name("Feature Name").content("Feature Content").user(user);

        // todo : change the endpoints to dynamically associate the user to the entity

        newMDLSForm = new MDLS().content("content").user(user);
    }

    ResponseEntity<Feature> response;

    @When("user selects create feature")
    public void user_selects_create_feature() {
        response = restClient
            .post()
            .uri("http://localhost:" + port + url)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .body(newFeatureForm)
            .retrieve()
            .toEntity(Feature.class);
    }

    @MGenerated // todo : think about usibng this in the test for assertions
    @Then("feature is created")
    public void feature_is_created() {
        assert response.getStatusCode() == HttpStatus.CREATED : "status code should be created, but is " + response.getStatusCode();
    }

    @MGenerated
    @Then("user is feature owner")
    public void user_is_feature_owner() {
        featureRepository.findById(response.getBody().getId()).ifPresent(System.out::println);
        //System.out.println(response);
        assert response.getBody().getUser().getLogin().equals(existingLogin) : "Feature did not get assigned to the test user";
    }

    @Given("user navigates to JDL specification view")
    public void user_navigates_to_jdl_specification_view() {
        url = "/api/mdls";
    }

    @Given("user selects new JDL specification option")
    public void user_selects_new_jdl_specification_option() {}

    ResponseEntity<MDLS> response2;

    RestClient.ResponseSpec response3;

    @When("user selects create JDL specification")
    public void user_selects_create_jdl_specification() {
        response2 = restClient
            .post()
            .uri("http://localhost:" + port + url)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .body(newFeatureForm)
            .retrieve()
            .toEntity(MDLS.class);
    }

    @Then("JDL specification is created")
    public void jdl_specification_is_created() {
        assert response2.getStatusCode() == HttpStatus.CREATED : "status code should be created, but is " + response2.getStatusCode();
    }

    @Then("user is JDL specification owner")
    public void user_is_jdl_specification_owner() {
        assert response2.getBody().getUser().getLogin().equals(existingLogin) : "MDLS did not get assigned to the test user";
    }

    @Given("user navigates to projects view : generating")
    public void user_navigates_to_projects_view_generating() {
        url = "/api/projects";
    }

    @Given("user selects new project option")
    public void user_selects_new_project_option() {}

    Project newProject;

    @Given("user fills the project form")
    public void user_fills_the_project_form() {
        newProject = new Project().name("Some project name").description("This is a test project").user(otherUser);
    }

    @Given("user selects JDL specification to use from the list he owns")
    public void user_selects_jdl_specification_to_use_from_the_list_he_owns() {
        List<MDLS> specs = restClient
            .get()
            .uri("http://localhost:" + port + "/api/mdls?filter=current-user")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(new ParameterizedTypeReference<List<MDLS>>() {});

        System.out.println("mdls returned: " + specs.get(0));
        newProject.setMdls(specs.get(0));
    }

    @Given("user selects feature files to use from the list he owns")
    public void user_selects_feature_files_to_use_from_the_list_he_owns() {
        List<Feature> specs = restClient
            .get()
            .uri("http://localhost:" + port + "/api/features?filter=current-user")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(new ParameterizedTypeReference<List<Feature>>() {});

        System.out.println("feature returned: " + specs.get(0));
        newProject.addFeature(specs.get(0));
    }

    @When("user clicks generate project")
    public void user_clicks_generate_project() {
        System.out.println(newProject);

        response3 = restClient
            .post()
            .uri("http://localhost:" + port + url)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .body(newProject)
            .retrieve();
    }

    Project generatedProject;

    @MGenerated
    @Then("project is generated")
    public void project_is_generated() throws InterruptedException {
        generatedProject = response3.toEntity(Project.class).getBody();
        System.out.println(generatedProject);
    }

    @MGenerated
    @Then("user is project owner")
    public void user_is_project_owner() {
        Project project = projectRepository.findById(generatedProject.getId()).orElseThrow();

        assert project.getUser().getId().equals(user.getId()) : "current user is not project owner. owner = " +
        project.getUser().getLogin();
    }

    @When("user clicks generate files button")
    public void user_clicks_generate_files_button() {
        // url is "/api/projects/" + chosenId from below stepdef
        response3 = restClient
            .post()
            .uri("http://localhost:" + port + url + "/generate-files")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .retrieve();
    }

    @Then("file generation process is started")
    public void file_generation_process_is_started() {
        HttpStatusCode statusCode = response3.toBodilessEntity().getStatusCode();
        assert statusCode == HttpStatus.ACCEPTED : "generate files was not accepted, got status: " + statusCode;
    }

    @Given("user navigates to their projects view")
    public void user_navigates_to_their_projects_view() {
        // get a list of all the projects
        response3 = restClient
            .get()
            .uri("http://localhost:" + port + "/api/projects?filter=current-user")
            .header("Authorization", "Bearer " + token)
            .retrieve();
        System.out.println(response3.body(new ParameterizedTypeReference<List<Project>>() {}));
    }

    Long chosenId;

    @Given("user selects project details")
    public void user_selects_project_details() {
        // get project from setup by id
        List<Project> projects = response3.body(new ParameterizedTypeReference<List<Project>>() {});
        chosenId = projects.stream().filter(project -> project.getId().equals(projectToGenerateId)).findFirst().orElseThrow().getId();
        url = "/api/projects/" + chosenId;
        response3 = restClient.get().uri("http://localhost:" + port + url).header("Authorization", "Bearer " + token).retrieve();

        System.out.println(response3.toEntity(Project.class));
    }

    byte[] fileBytes;

    @When("user clicks the download button")
    public void user_clicks_the_download_button() {
        fileBytes = restClient
            .get()
            .uri("http://localhost:" + port + "/api/projects/" + projectToDownloadId + "/download")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(byte[].class);
    }

    @Then("user receives project files")
    public void user_receives_project_files() throws IOException {
        Files.write(Paths.get(generatedProjectDirectory + "/test-project.zip"), fileBytes, StandardOpenOption.CREATE);
    }
}
