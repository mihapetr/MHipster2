package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.Project;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

public class Overview_of_personal_projectsStepDefs extends Common {

    @MGenerated
    static boolean done = false;

    String url;

    @MGenerated
    @Before
    public void beforeScenario() {
        if (!done) {
            done = true;
            setup();
        }
    }

    @Given("user is logged in : personal")
    public void user_is_logged_in_personal() {
        token = signIn(existingLogin, existingPassword);
    }

    @When("user navigates to projects view : personal")
    public void user_navigates_to_projects_view_personal() {
        url = "http://localhost:8080/api/projects";
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

    @Given("user selects a project they own")
    public void user_selects_a_project_they_own() {}

    @When("user selects project statistics of projects from list")
    public void user_selects_project_statistics_of_projects_from_list() {}

    @Then("user can see project code statistics")
    public void user_can_see_project_code_statistics() {}
}
