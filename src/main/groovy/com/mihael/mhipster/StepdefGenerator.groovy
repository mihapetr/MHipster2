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

	static void generateStepdefs(String featurePath, String stepdefsPath, String packageName) {

		def keywords = ['Given', 'When', 'Then', 'And']
		def steps = []
		def annotations = []
		def featureName = "MISSING Feature keyword"
		def methods = []

		new File(featurePath).eachLine { line ->
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
			annotations << annotation

			def cleaned = clean(step)

			methods <<
"""
	${annotation}
	public void ${cleaned} {

	}
"""
		}
		new File("${stepdefsPath}/${clean(featureName)}.java").text =
"""
package ${packageName}

class ${clean(featureName)} {

${methods.join()}

}
"""

	}
}
