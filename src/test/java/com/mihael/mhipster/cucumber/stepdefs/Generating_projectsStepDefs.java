package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.MDLS;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.MDLSRepository;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.service.dto.AdminUserDTO;
import com.mihael.mhipster.web.rest.vm.LoginVM;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Generating_projectsStepDefs extends Common {

    String url;
    User user, otherUser;

    @Autowired
    FeatureRepository featureRepository;

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

    @Then("user is feature owner")
    public void user_is_feature_owner() {
        featureRepository.findById(response.getBody().getId()).ifPresent(System.out::println);
        System.out.println(response);

        assert response.getBody().getUser().getLogin().equals(existingLogin) : "Feature did not get assigned to the test user";
    }

    @Given("user navigates to JDL specification view")
    public void user_navigates_to_jdl_specification_view() {
        url = "/api/mdls";
    }

    @Given("user selects new JDL specification option")
    public void user_selects_new_jdl_specification_option() {}

    ResponseEntity<MDLS> response2;

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

    @Given("user selects JDL specification to use from the list he owns")
    public void user_selects_jdl_specification_to_use_from_the_list_he_owns() {
        List<MDLS> specs = restClient
            .get()
            .uri("http://localhost:" + port + "/api/mdls?filter=current-user")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(new ParameterizedTypeReference<List<MDLS>>() {});

        System.out.println(specs);
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
    }

    @When("user clicks generate project")
    public void user_clicks_generate_project() {
        // post
    }

    @Then("project is generated using JHipster")
    public void project_is_generated_using_j_hipster() {
        // repository, status and response from jhipster / files in the destination directory
    }

    @Then("project is configured to use Cucumber and JaCoCo")
    public void project_is_configured_to_use_cucumber_and_ja_co_co() {
        // assert facts
    }

    @Then("user is project owner")
    public void user_is_project_owner() {
        // check user through repo find by id
    }

    @Given("user navigates to their projects view")
    public void user_navigates_to_their_projects_view() {
        // get a list of all the projects
    }

    @Given("user selects project details")
    public void user_selects_project_details() {
        // get project by one of the ids
    }

    @When("user clicks the download button")
    public void user_clicks_the_download_button() {
        // request files from the server
    }

    @Then("user receives project files")
    public void user_receives_project_files() {
        // destination mock files assert
    }
}
