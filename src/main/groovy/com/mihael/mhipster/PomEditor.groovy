package com.mihael.mhipster

class PomEditor {

	static void extend(String profileContent, String projectDir) {

		def pattern = ~/\s+<\/dependencies>/
		def index = 0
		def instance = 0
		def lines = new File(projectDir + "/backup_pom.xml").readLines()

		for (line in lines) {
			if (line =~ pattern) {
				instance ++
				if (instance == 2) break
			}
			index ++
		}
		println(index)

		lines.addAll(index, dependencies.readLines())


		pattern = ~/\s+<\/profiles>/
		index = 0
		lines.each {line ->
			if (line =~ pattern) instance = index
			index ++
		}

		lines.addAll(instance, profileContent.readLines())

		new File(projectDir + "/pom.xml").text = lines.join("\n")
	}

	static def dependencies =
'''				<dependency>
          	<groupId>org.junit.platform</groupId>
          	<artifactId>junit-platform-suite</artifactId>
          	<version>1.9.3</version>
          	<scope>test</scope>
        </dependency>
'''
}
