package com.mihael.mhipster

class CucumberSetup {

	static void configure(String templateContent, String projectDir, String packageName) {

		def configFilePath = "${projectDir}/src/test/java/${packageName.replace(".", "/")}/cucumber/TemplateCucumberIT.java"
		new File(configFilePath).text = templateContent
			.replace("__package-name-needle__", packageName)
	}
}
