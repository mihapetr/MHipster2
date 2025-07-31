package com.mihael.mhipster.cucumber.stepdefs;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.CodeStats;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.FeatureTst;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.FeatureTstRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.service.UserService;
import com.mihael.mhipster.service.dto.AdminUserDTO;
import com.mihael.mhipster.web.rest.AuthenticateController;
import com.mihael.mhipster.web.rest.vm.LoginVM;
import io.cucumber.java.Before;
import java.util.ArrayList;
import java.util.List;
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
    FeatureRepository featureRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FeatureTstRepository featureTstRepository;

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

    protected void conjureFullFeatureTst(User user, Double statsMod, Project project, int nOfFeatures) {
        CodeStats codeStats = new CodeStats()
            .branches(0.1 + statsMod)
            .deadBranches(0.2 + statsMod)
            .classes(0.3 + statsMod)
            .deadClasses(0.4 + statsMod)
            .instructions(0.6 + statsMod)
            .deadInstructions(0.7 + statsMod)
            .lines(0.8 + statsMod)
            .deadLines(0.6 + statsMod)
            .methods(0.0 + statsMod)
            .deadMethods(0.1 + statsMod);

        if (project == null) {
            project = new Project();
            project.setUser(user);
            project.name("Some Project" + statsMod);
            project.description("Some Description");
            project = projectRepository.save(project);
        }

        List<Feature> features = new ArrayList<>();

        for (int i = 0; i < nOfFeatures; i++) {
            Feature feature = new Feature().name("feature (" + i + ") of project " + project.getId()).user(user).content("some content");
            features.add(featureRepository.save(feature));
        }

        FeatureTst featureTst = new FeatureTst().parent(codeStats).project(project);
        for (Feature feature : features) {
            featureTst.addFeature(feature);
        }
        featureTstRepository.save(featureTst);
    }
}
