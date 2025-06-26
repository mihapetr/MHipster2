package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.FeatureTestSamples.*;
import static com.mihael.mhipster.domain.FeatureTstTestSamples.*;
import static com.mihael.mhipster.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feature.class);
        Feature feature1 = getFeatureSample1();
        Feature feature2 = new Feature();
        assertThat(feature1).isNotEqualTo(feature2);

        feature2.setId(feature1.getId());
        assertThat(feature1).isEqualTo(feature2);

        feature2 = getFeatureSample2();
        assertThat(feature1).isNotEqualTo(feature2);
    }

    @Test
    void projectTest() {
        Feature feature = getFeatureRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        feature.addProject(projectBack);
        assertThat(feature.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getFeatures()).containsOnly(feature);

        feature.removeProject(projectBack);
        assertThat(feature.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getFeatures()).doesNotContain(feature);

        feature.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(feature.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getFeatures()).containsOnly(feature);

        feature.setProjects(new HashSet<>());
        assertThat(feature.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getFeatures()).doesNotContain(feature);
    }

    @Test
    void featureTstTest() {
        Feature feature = getFeatureRandomSampleGenerator();
        FeatureTst featureTstBack = getFeatureTstRandomSampleGenerator();

        feature.addFeatureTst(featureTstBack);
        assertThat(feature.getFeatureTsts()).containsOnly(featureTstBack);
        assertThat(featureTstBack.getFeatures()).containsOnly(feature);

        feature.removeFeatureTst(featureTstBack);
        assertThat(feature.getFeatureTsts()).doesNotContain(featureTstBack);
        assertThat(featureTstBack.getFeatures()).doesNotContain(feature);

        feature.featureTsts(new HashSet<>(Set.of(featureTstBack)));
        assertThat(feature.getFeatureTsts()).containsOnly(featureTstBack);
        assertThat(featureTstBack.getFeatures()).containsOnly(feature);

        feature.setFeatureTsts(new HashSet<>());
        assertThat(feature.getFeatureTsts()).doesNotContain(featureTstBack);
        assertThat(featureTstBack.getFeatures()).doesNotContain(feature);
    }
}
