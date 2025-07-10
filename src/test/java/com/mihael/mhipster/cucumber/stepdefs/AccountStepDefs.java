package com.mihael.mhipster.cucumber.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.service.UserService;
import com.mihael.mhipster.service.dto.AdminUserDTO;
import com.mihael.mhipster.web.rest.AuthenticateController;
import com.mihael.mhipster.web.rest.vm.LoginVM;
import com.mihael.mhipster.web.rest.vm.ManagedUserVM;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

public class AccountStepDefs extends Common {

    private final String login = testLogin;
    private final String password = testPassword;

    @Before
    public void before() {
        setup();
    }

    @Given("user does not have account")
    public void user_does_not_have_account() {
        assert userRepository.findOneByLogin(login).isEmpty() : "user with login=" + login + " does have an account";
    }

    private ResponseEntity<Void> response;

    @When("user submits registration form")
    public void user_submits_registration_form() {
        ManagedUserVM registrationForm = new ManagedUserVM();
        registrationForm.setLogin(login);
        registrationForm.setEmail("email@email.com");
        registrationForm.setPassword(password);

        response = restClient
            .post()
            .uri("http://localhost:" + port + "/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(registrationForm)
            .retrieve()
            .toBodilessEntity();
    }

    @Then("user profile is created")
    public void user_profile_is_created() {
        assert userRepository.findOneByLogin(login).isPresent() : "user with login=" + login + " does not have an account";
    }

    @Then("user is notified about success")
    public void user_is_notified_about_success() {
        assert HttpStatus.CREATED == response.getStatusCode() : "there was an error while creating an account" +
        "error=" +
        response.getStatusCode();
    }

    LoginVM loginForm = new LoginVM();

    @Given("credentials are valid")
    public void credentials_are_valid() {
        loginForm.setUsername(existingLogin);
        loginForm.setPassword(existingPassword);
    }

    private ResponseEntity<AuthenticateController.JWTToken> loginResponse;
    private String actualUnauthorizedBodyJson;

    @When("user submits login form")
    public void user_submits_login_form() {
        try {
            loginResponse = signIn(loginForm);
        } catch (HttpClientErrorException.Unauthorized ex) {
            actualUnauthorizedBodyJson = ex.getResponseBodyAsString();
        }
    }

    @Then("user is logged in : account")
    public void user_is_logged_in_account() {
        String token = loginResponse.getBody().getIdToken();
        assert token != null && !token.isEmpty() : "did not receive JWT from server";
        // consider writing a test to see if the token is usable
    }

    ObjectMapper mapper = new ObjectMapper();

    @Given("credentials are invalid")
    public void credentials_are_invalid() {
        loginForm.setUsername(existingLogin);
        loginForm.setPassword("gibberish");
    }

    @Then("login is rejected")
    public void login_is_rejected() throws JsonProcessingException {
        JsonNode actualNode = mapper.readTree(actualUnauthorizedBodyJson);
        assert "401".equals(actualNode.get("status").asText()) : "expected status 401 from server, got " +
        actualNode.get("status").asText();
    }

    @Then("user is notified about failure")
    public void user_is_notified_about_failure() throws JsonProcessingException {
        JsonNode actualNode = mapper.readTree(actualUnauthorizedBodyJson);
        assert "Bad credentials".equals(actualNode.get("detail").asText()) : "expected 'Bad credentials' from server, got " +
        actualNode.get("detail").asText();
    }
}
