package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.CodeStatsTestSamples.*;
import static com.mihael.mhipster.domain.FeatureTestSamples.*;
import static com.mihael.mhipster.domain.FeatureTstTestSamples.*;
import static com.mihael.mhipster.domain.ProjectTestSamples.*;
import static com.mihael.mhipster.domain.TestReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeatureTstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeatureTst.class);
        FeatureTst featureTst1 = getFeatureTstSample1();
        FeatureTst featureTst2 = new FeatureTst();
        assertThat(featureTst1).isNotEqualTo(featureTst2);

        featureTst2.setId(featureTst1.getId());
        assertThat(featureTst1).isEqualTo(featureTst2);

        featureTst2 = getFeatureTstSample2();
        assertThat(featureTst1).isNotEqualTo(featureTst2);
    }

    @Test
    void parentTest() {
        FeatureTst featureTst = getFeatureTstRandomSampleGenerator();
        CodeStats codeStatsBack = getCodeStatsRandomSampleGenerator();

        featureTst.setParent(codeStatsBack);
        assertThat(featureTst.getParent()).isEqualTo(codeStatsBack);

        featureTst.parent(null);
        assertThat(featureTst.getParent()).isNull();
    }

    @Test
    void testReportTest() {
        FeatureTst featureTst = getFeatureTstRandomSampleGenerator();
        TestReport testReportBack = getTestReportRandomSampleGenerator();

        featureTst.addTestReport(testReportBack);
        assertThat(featureTst.getTestReports()).containsOnly(testReportBack);
        assertThat(testReportBack.getFeatureTst()).isEqualTo(featureTst);

        featureTst.removeTestReport(testReportBack);
        assertThat(featureTst.getTestReports()).doesNotContain(testReportBack);
        assertThat(testReportBack.getFeatureTst()).isNull();

        featureTst.testReports(new HashSet<>(Set.of(testReportBack)));
        assertThat(featureTst.getTestReports()).containsOnly(testReportBack);
        assertThat(testReportBack.getFeatureTst()).isEqualTo(featureTst);

        featureTst.setTestReports(new HashSet<>());
        assertThat(featureTst.getTestReports()).doesNotContain(testReportBack);
        assertThat(testReportBack.getFeatureTst()).isNull();
    }

    @Test
    void featureTest() {
        FeatureTst featureTst = getFeatureTstRandomSampleGenerator();
        Feature featureBack = getFeatureRandomSampleGenerator();

        featureTst.addFeature(featureBack);
        assertThat(featureTst.getFeatures()).containsOnly(featureBack);

        featureTst.removeFeature(featureBack);
        assertThat(featureTst.getFeatures()).doesNotContain(featureBack);

        featureTst.features(new HashSet<>(Set.of(featureBack)));
        assertThat(featureTst.getFeatures()).containsOnly(featureBack);

        featureTst.setFeatures(new HashSet<>());
        assertThat(featureTst.getFeatures()).doesNotContain(featureBack);
    }

    @Test
    void projectTest() {
        FeatureTst featureTst = getFeatureTstRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        featureTst.setProject(projectBack);
        assertThat(featureTst.getProject()).isEqualTo(projectBack);

        featureTst.project(null);
        assertThat(featureTst.getProject()).isNull();
    }
}
