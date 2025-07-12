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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class Generating_projectsStepDefs extends Common {

    String url;
    User user, otherUser;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MDLSRepository mdlsRepository;

    static boolean done = false;

    @MGenerated
    @Before
    public void before() {
        setup();
        user = userRepository.findOneByLogin(existingLogin).orElseThrow();
        otherUser = userRepository.findOneByLogin("user").orElseThrow();

        if (!done) {
            done = true;
            Feature feature = new Feature().name("Other user's feature").content("Other Feature content").user(otherUser);

            MDLS mdls = new MDLS().content("Other user's MDLS content").user(otherUser);

            featureRepository.save(feature);
            mdlsRepository.save(mdls);
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

        System.out.println(specs);
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

        System.out.println(specs);
        newProject.addFeature(specs.get(0));
    }

    @When("user clicks generate project")
    public void user_clicks_generate_project() {
        response3 = restClient
            .post()
            .uri("http://localhost:" + port + url)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .body(newProject)
            .retrieve();
    }

    @Then("project is generated using JHipster")
    public void project_is_generated_using_j_hipster() {
        // todo : repository, status and response from jhipster / files in the destination directory
        //Project.generate();
    }

    @Then("project is configured to use Cucumber and JaCoCo")
    public void project_is_configured_to_use_cucumber_and_ja_co_co() {
        // todo : assert facts
    }

    @MGenerated
    @Then("user is project owner")
    public void user_is_project_owner() {
        Project project = projectRepository.findById(response3.toEntity(Project.class).getBody().getId()).orElseThrow();

        assert project.getUser().getId().equals(user.getId()) : "current user is not project owner. owner = " +
        project.getUser().getLogin();
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

    @Given("user selects project details")
    public void user_selects_project_details() {
        // get project by one of the ids
        Long chosenId = response3.body(new ParameterizedTypeReference<List<Project>>() {}).get(0).getId();

        response3 = restClient
            .get()
            .uri("http://localhost:" + port + "/api/projects/" + chosenId)
            .header("Authorization", "Bearer " + token)
            .retrieve();

        System.out.println(response3.toEntity(Project.class));
    }

    @When("user clicks the download button")
    public void user_clicks_the_download_button() {
        // request files from the server : POST
    }

    @Then("user receives project files")
    public void user_receives_project_files() {
        // todo : after implementation : destination mock files assert
    }
}
