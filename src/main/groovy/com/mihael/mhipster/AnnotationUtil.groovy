package com.mihael.mhipster

@MGenerated
class AnnotationUtil {

	static changeRetentionPolicy(String annotationClassPath, String newPolicy) {

		if (!['SOURCE', 'RUNTIME'].contains(newPolicy)) {
			println "Error: Retention policy must be either SOURCE or RUNTIME"
			System.exit(1)
		}

		def javaFile = new File(annotationClassPath)
		if (!javaFile.exists()) {
			println "Error: File not found - $annotationClassPath"
			System.exit(1)
		}

		def modified = false
		def lines = javaFile.readLines().collect { line ->
			if (line.contains('@Retention(RetentionPolicy.SOURCE)') || line.contains('@Retention(RetentionPolicy.RUNTIME)')) {
				modified = true
				return "@Retention(RetentionPolicy.${newPolicy})"
			}
			return line
		}

		if (modified) {
			javaFile.text = lines.join('\n') + '\n'
			println "Modified retention policy in $annotationClassPath to $newPolicy"
		} else {
			println "No @Retention annotation found in $annotationClassPath"
		}
	}
}
