package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.service.UserService;
import com.mihael.mhipster.service.dto.AdminUserDTO;
import com.mihael.mhipster.web.rest.AuthenticateController;
import com.mihael.mhipster.web.rest.vm.LoginVM;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@MGenerated
public class Common {

    protected final String generatedProjectDirectory = "/home/mihael/Projects/MHipster-generated-projects";

    protected final String testLogin = "myUsername";
    protected final String testPassword = "myPassword";

    protected final String existingLogin = "existinglogin";
    protected final String existingPassword = "existingpassword";

    protected String token;
    RestClient.ResponseSpec res;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @LocalServerPort
    protected int port;

    @Autowired
    protected RestClient restClient;

    public void setup() {
        if (userRepository.findOneByLogin(existingLogin).isEmpty()) {
            AdminUserDTO user = new AdminUserDTO();
            user.setLogin(existingLogin);
            user.setEmail("existing@email.com");
            userService.registerUser(user, existingPassword);
        }
    }

    protected ResponseEntity<AuthenticateController.JWTToken> signIn(LoginVM loginForm) throws HttpClientErrorException {
        return restClient
            .post()
            .uri("http://localhost:" + port + "/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .body(loginForm)
            .retrieve()
            .toEntity(AuthenticateController.JWTToken.class);
    }

    protected String signIn(String username, String password) throws HttpClientErrorException {
        LoginVM loginForm = new LoginVM();
        loginForm.setUsername(username);
        loginForm.setPassword(password);
        return signIn(loginForm).getBody().getIdToken();
    }
}
