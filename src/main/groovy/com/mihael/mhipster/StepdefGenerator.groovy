package com.mihael.mhipster

@MGenerated
class StepdefGenerator {

	static String clean(step) {
		// Replace all non-word characters (except $) with underscores
		def cleaned = step.replaceAll('[^\\w$]', "_")
		// Remove leading digits and underscores
		cleaned = cleaned.replaceAll('^[^a-zA-Z_$]+', "")
		// If string is now empty or starts with a digit, prepend underscore
		if (!cleaned || !cleaned[0].matches('[a-zA-Z_$]')) {
			cleaned = "_${cleaned}"
		}
		return cleaned
	}

	static void generateStepdefs(Long featureId, String featureContent, String projectRoot, String packageName) {

		def stepdefsPath = projectRoot + "/src/test/java/${packageName.replace(".","/")}/cucumber/stepdefs"
		def featuresPath = projectRoot + "/src/test/resources/features"

		def keywords = ['Given', 'When', 'Then', 'And']
		def steps = []
		def annotations = []
		def featureName = "MISSING Feature keyword"
		def methods = []

		featureContent.eachLine { line ->
			keywords.each { keyword ->
				if (line.trim().startsWith(keyword)) {
					// Extract content after the keyword
					def step = line.trim().substring(keyword.length()).trim()
					steps << [keyword, step]
				}
			}
			if (line.trim().startsWith("Feature:"))
				featureName = line.trim().substring("Feature:".length()).trim()
		}
		steps.each { keyword, step ->
			def annotation = "@${keyword}(\"${step}\")"
			if (!(annotation in annotations)) {
				annotations << annotation

				def cleaned = clean(step)

				methods <<
"""
	${annotation}
	public void ${cleaned}() {

	}
"""
			}

		}
		new File("${featuresPath}/__${featureId}__${clean(featureName)}.feature").text = featureContent
		new File("${stepdefsPath}/${clean(featureName)}.java").text =
"""
package ${packageName}.cucumber.stepdefs;

import io.cucumber.java.en.*;

public class ${clean(featureName)} {

${methods.join()}

}
"""

	}
}
