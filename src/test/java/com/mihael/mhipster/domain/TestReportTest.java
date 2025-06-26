package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.FeatureTstTestSamples.*;
import static com.mihael.mhipster.domain.TestReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestReport.class);
        TestReport testReport1 = getTestReportSample1();
        TestReport testReport2 = new TestReport();
        assertThat(testReport1).isNotEqualTo(testReport2);

        testReport2.setId(testReport1.getId());
        assertThat(testReport1).isEqualTo(testReport2);

        testReport2 = getTestReportSample2();
        assertThat(testReport1).isNotEqualTo(testReport2);
    }

    @Test
    void featureTstTest() {
        TestReport testReport = getTestReportRandomSampleGenerator();
        FeatureTst featureTstBack = getFeatureTstRandomSampleGenerator();

        testReport.setFeatureTst(featureTstBack);
        assertThat(testReport.getFeatureTst()).isEqualTo(featureTstBack);

        testReport.featureTst(null);
        assertThat(testReport.getFeatureTst()).isNull();
    }
}
