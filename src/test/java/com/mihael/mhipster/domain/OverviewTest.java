package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.CodeStatsTestSamples.*;
import static com.mihael.mhipster.domain.OverviewTestSamples.*;
import static com.mihael.mhipster.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OverviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Overview.class);
        Overview overview1 = getOverviewSample1();
        Overview overview2 = new Overview();
        assertThat(overview1).isNotEqualTo(overview2);

        overview2.setId(overview1.getId());
        assertThat(overview1).isEqualTo(overview2);

        overview2 = getOverviewSample2();
        assertThat(overview1).isNotEqualTo(overview2);
    }

    @Test
    void parentTest() {
        Overview overview = getOverviewRandomSampleGenerator();
        CodeStats codeStatsBack = getCodeStatsRandomSampleGenerator();

        overview.setParent(codeStatsBack);
        assertThat(overview.getParent()).isEqualTo(codeStatsBack);

        overview.parent(null);
        assertThat(overview.getParent()).isNull();
    }

    @Test
    void projectTest() {
        Overview overview = getOverviewRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        overview.addProject(projectBack);
        assertThat(overview.getProjects()).containsOnly(projectBack);

        overview.removeProject(projectBack);
        assertThat(overview.getProjects()).doesNotContain(projectBack);

        overview.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(overview.getProjects()).containsOnly(projectBack);

        overview.setProjects(new HashSet<>());
        assertThat(overview.getProjects()).doesNotContain(projectBack);
    }
}
