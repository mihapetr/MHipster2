package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.FeatureTestSamples.*;
import static com.mihael.mhipster.domain.FeatureTstTestSamples.*;
import static com.mihael.mhipster.domain.MDLSTestSamples.*;
import static com.mihael.mhipster.domain.OverviewTestSamples.*;
import static com.mihael.mhipster.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void mdlsTest() {
        Project project = getProjectRandomSampleGenerator();
        MDLS mDLSBack = getMDLSRandomSampleGenerator();

        project.setMdls(mDLSBack);
        assertThat(project.getMdls()).isEqualTo(mDLSBack);

        project.mdls(null);
        assertThat(project.getMdls()).isNull();
    }

    @Test
    void featureTstTest() {
        Project project = getProjectRandomSampleGenerator();
        FeatureTst featureTstBack = getFeatureTstRandomSampleGenerator();

        project.addFeatureTst(featureTstBack);
        assertThat(project.getFeatureTsts()).containsOnly(featureTstBack);
        assertThat(featureTstBack.getProject()).isEqualTo(project);

        project.removeFeatureTst(featureTstBack);
        assertThat(project.getFeatureTsts()).doesNotContain(featureTstBack);
        assertThat(featureTstBack.getProject()).isNull();

        project.featureTsts(new HashSet<>(Set.of(featureTstBack)));
        assertThat(project.getFeatureTsts()).containsOnly(featureTstBack);
        assertThat(featureTstBack.getProject()).isEqualTo(project);

        project.setFeatureTsts(new HashSet<>());
        assertThat(project.getFeatureTsts()).doesNotContain(featureTstBack);
        assertThat(featureTstBack.getProject()).isNull();
    }

    @Test
    void featureTest() {
        Project project = getProjectRandomSampleGenerator();
        Feature featureBack = getFeatureRandomSampleGenerator();

        project.addFeature(featureBack);
        assertThat(project.getFeatures()).containsOnly(featureBack);

        project.removeFeature(featureBack);
        assertThat(project.getFeatures()).doesNotContain(featureBack);

        project.features(new HashSet<>(Set.of(featureBack)));
        assertThat(project.getFeatures()).containsOnly(featureBack);

        project.setFeatures(new HashSet<>());
        assertThat(project.getFeatures()).doesNotContain(featureBack);
    }

    @Test
    void overviewTest() {
        Project project = getProjectRandomSampleGenerator();
        Overview overviewBack = getOverviewRandomSampleGenerator();

        project.addOverview(overviewBack);
        assertThat(project.getOverviews()).containsOnly(overviewBack);
        assertThat(overviewBack.getProjects()).containsOnly(project);

        project.removeOverview(overviewBack);
        assertThat(project.getOverviews()).doesNotContain(overviewBack);
        assertThat(overviewBack.getProjects()).doesNotContain(project);

        project.overviews(new HashSet<>(Set.of(overviewBack)));
        assertThat(project.getOverviews()).containsOnly(overviewBack);
        assertThat(overviewBack.getProjects()).containsOnly(project);

        project.setOverviews(new HashSet<>());
        assertThat(project.getOverviews()).doesNotContain(overviewBack);
        assertThat(overviewBack.getProjects()).doesNotContain(project);
    }
}
