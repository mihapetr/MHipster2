package com.mihael.mhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TestReportAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestReportAllPropertiesEquals(TestReport expected, TestReport actual) {
        assertTestReportAutoGeneratedPropertiesEquals(expected, actual);
        assertTestReportAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestReportAllUpdatablePropertiesEquals(TestReport expected, TestReport actual) {
        assertTestReportUpdatableFieldsEquals(expected, actual);
        assertTestReportUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestReportAutoGeneratedPropertiesEquals(TestReport expected, TestReport actual) {
        assertThat(expected)
            .as("Verify TestReport auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestReportUpdatableFieldsEquals(TestReport expected, TestReport actual) {
        assertThat(expected)
            .as("Verify TestReport relevant properties")
            .satisfies(e -> assertThat(e.getHtml()).as("check html").isEqualTo(actual.getHtml()))
            .satisfies(e -> assertThat(e.getRuntimeRetention()).as("check runtimeRetention").isEqualTo(actual.getRuntimeRetention()))
            .satisfies(e -> assertThat(e.getMissedInstructions()).as("check missedInstructions").isEqualTo(actual.getMissedInstructions()))
            .satisfies(e -> assertThat(e.getInstructions()).as("check instructions").isEqualTo(actual.getInstructions()))
            .satisfies(e -> assertThat(e.getMissedBranches()).as("check missedBranches").isEqualTo(actual.getMissedBranches()))
            .satisfies(e -> assertThat(e.getBranches()).as("check branches").isEqualTo(actual.getBranches()))
            .satisfies(e -> assertThat(e.getMissedLines()).as("check missedLines").isEqualTo(actual.getMissedLines()))
            .satisfies(e -> assertThat(e.getLines()).as("check lines").isEqualTo(actual.getLines()))
            .satisfies(e -> assertThat(e.getMissedMethods()).as("check missedMethods").isEqualTo(actual.getMissedMethods()))
            .satisfies(e -> assertThat(e.getMethods()).as("check methods").isEqualTo(actual.getMethods()))
            .satisfies(e -> assertThat(e.getMissedClasses()).as("check missedClasses").isEqualTo(actual.getMissedClasses()))
            .satisfies(e -> assertThat(e.getClasses()).as("check classes").isEqualTo(actual.getClasses()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestReportUpdatableRelationshipsEquals(TestReport expected, TestReport actual) {
        assertThat(expected)
            .as("Verify TestReport relationships")
            .satisfies(e -> assertThat(e.getFeatureTst()).as("check featureTst").isEqualTo(actual.getFeatureTst()));
    }
}
