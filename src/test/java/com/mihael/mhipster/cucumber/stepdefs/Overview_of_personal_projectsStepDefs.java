package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.*;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.FeatureTstRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

public class Overview_of_personal_projectsStepDefs extends Common {

    @MGenerated
    static boolean done = false;

    User user;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FeatureTstRepository featureTstRepository;

    String url;

    @MGenerated
    @Before
    public void beforeScenario() {
        user = userRepository.findOneByLogin(existingLogin).orElse(null);
        if (!done) {
            done = true;
            setup();
            user = userRepository.findOneByLogin(existingLogin).orElseThrow();

            conjureFullFeatureTst(user, 0.0, null, 1);
        }
    }

    @Given("user is logged in : personal")
    public void user_is_logged_in_personal() {
        token = signIn(existingLogin, existingPassword);
    }

    @When("user navigates to projects view : personal")
    public void user_navigates_to_projects_view_personal() {
        url = "http://localhost:" + port + "/api/projects?filter=current-user";
        res = restClient.get().uri(url).header("Authorization", "Bearer " + token).retrieve();
    }

    @MGenerated
    @Then("user can see all projects they own")
    public void user_can_see_all_projects_they_own() {
        res
            .toEntity(new ParameterizedTypeReference<List<Project>>() {})
            .getBody()
            .forEach(project -> {
                assert project.getUser().getLogin().equals(existingLogin);
            });
    }

    List<FeatureTst> featureTsts;
    Long selectedId;

    @Given("user navigates to feature tests view")
    public void user_navigates_to_feature_tests_view() {
        url = "http://localhost:" + port + "/api/feature-tsts?filter=current-user";
        res = restClient.get().uri(url).header("Authorization", "Bearer " + token).retrieve();
        featureTsts = res.toEntity(new ParameterizedTypeReference<List<FeatureTst>>() {}).getBody();
    }

    @When("user selects a feature test from list")
    public void user_selects_a_feature_test_from_list() {
        selectedId = featureTsts.stream().findFirst().orElseThrow().getId();
    }

    @Then("user can see code statistics of features from that test")
    public void user_can_see_code_statistics_of_features_from_that_test() {
        url = "http://localhost:" + port + "/api/feature-tsts/" + selectedId;
        res = restClient.get().uri(url).header("Authorization", "Bearer " + token).retrieve();
        CodeStats codeStats = res.toEntity(FeatureTst.class).getBody().getParent();
        //System.out.println(codeStats.toString());
    }
}
