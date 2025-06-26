package com.mihael.mhipster.domain;

import static com.mihael.mhipster.domain.MDLSTestSamples.*;
import static com.mihael.mhipster.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mihael.mhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MDLSTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MDLS.class);
        MDLS mDLS1 = getMDLSSample1();
        MDLS mDLS2 = new MDLS();
        assertThat(mDLS1).isNotEqualTo(mDLS2);

        mDLS2.setId(mDLS1.getId());
        assertThat(mDLS1).isEqualTo(mDLS2);

        mDLS2 = getMDLSSample2();
        assertThat(mDLS1).isNotEqualTo(mDLS2);
    }

    @Test
    void projectTest() {
        MDLS mDLS = getMDLSRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        mDLS.setProject(projectBack);
        assertThat(mDLS.getProject()).isEqualTo(projectBack);
        assertThat(projectBack.getMdls()).isEqualTo(mDLS);

        mDLS.project(null);
        assertThat(mDLS.getProject()).isNull();
        assertThat(projectBack.getMdls()).isNull();
    }
}
