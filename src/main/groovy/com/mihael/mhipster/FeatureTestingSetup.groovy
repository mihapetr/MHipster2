package com.mihael.mhipster

class FeatureTestingSetup {

	static void configure(String projectDir, String baseUrl, String notGeneratedDir, Long projectId) {

		new File(projectDir + "/mhipster/test_features.conf").text =
"""
BASE_URL="${baseUrl}"
NOT_GENERATED_DIR="${notGeneratedDir}"
PROJECT_ID="${projectId}"
"""
	}
}
