package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.Overview;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.domain.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.io.IOException;
import java.util.List;
import org.hibernate.sql.ast.tree.expression.Over;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Platform_statistics_overviewStepDefs extends Common {

    User user, admin;
    static boolean done;

    String adminLogin = "admin";
    String adminPassword = "admin";

    @Before
    public void before() throws IOException {
        user = userRepository.findOneByLogin(existingLogin).orElseThrow();
        admin = userRepository.findOneByLogin("admin").orElseThrow();

        if (!done) {
            done = true;
            setup();

            Project project = new Project().user(user).name("Overview test project");
            project = projectRepository.save(project);
            conjureFullFeatureTst(user, 0.0, project, 1); // will not be in overview
            conjureFullFeatureTst(user, 0.2, project, 2); // will be in overview
            conjureFullFeatureTst(user, 0.0, null, 1); // will be in overview
        }
    }

    @Given("administrator is logged in")
    public void administrator_is_logged_in() {
        token = signIn(adminLogin, adminPassword);
    }

    String url;
    List<Overview> overviews;

    @Given("admin navigates to overview section")
    public void admin_navigates_to_overview_section() {
        url = "http://localhost:" + port + "/api/overviews";
        overviews = restClient
            .get()
            .uri(url)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<Overview>>() {})
            .getBody();
    }

    ResponseEntity<Overview> generatedOverviewResponse;

    @Given("admin selects create overview option")
    public void admin_selects_create_overview_option() {
        generatedOverviewResponse = restClient
            .post()
            .uri(url + "/generate")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .toEntity(Overview.class);
    }

    @Then("platform statistics overview is created")
    public void platform_statistics_overview_is_created() {
        assert generatedOverviewResponse.getStatusCode() == HttpStatus.CREATED;
    }

    @Then("admin is overview owner")
    public void admin_is_overview_owner() {
        assert generatedOverviewResponse.getBody().getUser().getLogin().equals(admin.getLogin()) : "responsebody= " +
        generatedOverviewResponse.getBody();
    }

    Long selectedFeatureTstId;

    @When("admin selects details of one of the overviews")
    public void admin_selects_details_of_one_of_the_overviews() {
        selectedFeatureTstId = overviews.stream().findFirst().orElseThrow().getId();
    }

    @Then("admin can see overview details")
    public void admin_can_see_overview_details() {
        Overview overview = restClient
            .get()
            .uri(url + "/" + selectedFeatureTstId)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .toEntity(Overview.class)
            .getBody();
        System.out.println(overview);
    }
}
