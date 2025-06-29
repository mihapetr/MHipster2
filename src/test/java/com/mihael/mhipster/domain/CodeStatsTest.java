package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.CodeStatsTestSamples.*;
import static com.mihael.mhipster.domain.FeatureTstTestSamples.*;
import static com.mihael.mhipster.domain.OverviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeStatsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeStats.class);
        CodeStats codeStats1 = getCodeStatsSample1();
        CodeStats codeStats2 = new CodeStats();
        assertThat(codeStats1).isNotEqualTo(codeStats2);

        codeStats2.setId(codeStats1.getId());
        assertThat(codeStats1).isEqualTo(codeStats2);

        codeStats2 = getCodeStatsSample2();
        assertThat(codeStats1).isNotEqualTo(codeStats2);
    }

    @Test
    void featureTstTest() {
        CodeStats codeStats = getCodeStatsRandomSampleGenerator();
        FeatureTst featureTstBack = getFeatureTstRandomSampleGenerator();

        codeStats.setFeatureTst(featureTstBack);
        assertThat(codeStats.getFeatureTst()).isEqualTo(featureTstBack);
        assertThat(featureTstBack.getParent()).isEqualTo(codeStats);

        codeStats.featureTst(null);
        assertThat(codeStats.getFeatureTst()).isNull();
        assertThat(featureTstBack.getParent()).isNull();
    }

    @Test
    void overviewTest() {
        CodeStats codeStats = getCodeStatsRandomSampleGenerator();
        Overview overviewBack = getOverviewRandomSampleGenerator();

        codeStats.setOverview(overviewBack);
        assertThat(codeStats.getOverview()).isEqualTo(overviewBack);
        assertThat(overviewBack.getParent()).isEqualTo(codeStats);

        codeStats.overview(null);
        assertThat(codeStats.getOverview()).isNull();
        assertThat(overviewBack.getParent()).isNull();
    }
}
