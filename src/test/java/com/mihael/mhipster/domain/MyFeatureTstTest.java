package com.mihael.mhipster.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class MyFeatureTstTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void generateCodeStats() throws Exception {
        String runtime_JSON =
            """
            {
              "runtimeRetention" : true,
              "missedInstructions": 7006,
              "instructions": 8163,
              "missedBranches": 556,
              "branches": 572,
              "missedLines": 1777,
              "lines": 2037,
              "missedMethods": 579,
              "methods": 673,
              "missedClasses": 36,
              "classes": 82
            }""";

        runtime_JSON = """
        {
          "runtimeRetention" : true,
          "missedInstructions": 5,
          "instructions": 10,
          "missedBranches": 5,
          "branches": 10,
          "missedLines": 5,
          "lines": 10,
          "missedMethods": 5,
          "methods": 10,
          "missedClasses": 5,
          "classes": 10
        }""";

        TestReport runtimeReport = objectMapper.readValue(runtime_JSON, TestReport.class);

        String source_JSON =
            """
            {
              "runtimeRetention" : false,
              "missedInstructions": 7006,
              "instructions": 9163,
              "missedBranches": 576,
              "branches": 672,
              "missedLines": 1997,
              "lines": 3037,
              "missedMethods": 579,
              "methods": 873,
              "missedClasses": 38,
              "classes": 89
            }""";

        source_JSON = """
        {
          "runtimeRetention" : false,
          "missedInstructions": 5,
          "instructions": 15,
          "missedBranches": 5,
          "branches": 20,
          "missedLines": 5,
          "lines": 10,
          "missedMethods": 5,
          "methods": 11,
          "missedClasses": 5,
          "classes": 12
        }""";

        TestReport sourceReport = objectMapper.readValue(source_JSON, TestReport.class);

        FeatureTst featureTst = new FeatureTst().parent(new CodeStats()).addTestReport(sourceReport).addTestReport(runtimeReport);

        featureTst.generateStats();
        System.out.println(featureTst.getParent());
    }
}
