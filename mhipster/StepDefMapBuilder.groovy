class StepDefMapBuilder {

  static void main(String[] args) {
    def stepMap = StepDefMapBuilder.extractStepDefsByFeature("../skeleton-output.txt")

    stepMap.each { feature, methods ->
      def className = feature.trim().replaceAll("[^a-zA-Z0-9]", "_").capitalize() + "StepDefs"
      def fileName = "${className}.java"

      def classBody = """
        package com.mihael.mhipster.cucumber.stepdefs;

        import io.cucumber.java.en.*;

        public class ${className} {

${methods.collect { "    " + it.replaceAll("\n", "\n    ") }.join("\n\n")}
        }
    """.stripIndent().trim()

      def location = "../src/test/java/com/mihael/mhipster/cucumber/stepdefs/"

      new File(location + fileName).text = classBody
      println "✅ Generated ${fileName} with ${methods.size()} step definition(s)."
    }

  }

  static Map<String, Set<String>> extractStepDefsByFeature(String filePath) {
    def featureSteps = [:].withDefault { new LinkedHashSet<String>() }
    def lines = new File(filePath).readLines()

    def featurePattern = ~/^\[ERROR\] ([^.]+)\.[^ ]+.*<<< ERROR!/
    def stepAnnotationPattern = ~/^@(Given|When|Then)\(".*"\)/
    def currentFeature = null
    boolean collecting = false
    List<String> methodBuffer = []

    for (int i = 0; i < lines.size(); i++) {
      String line = lines[i]

      // Start of a feature error block
      def featureMatch = (line =~ featurePattern)
      if (featureMatch.find()) {
        currentFeature = featureMatch.group(1).trim()
        collecting = false
        continue
      }

      // Look for the start of a method block
      if (line.startsWith("[INFO] Results:")) break
      if (currentFeature && (line =~ stepAnnotationPattern)) {
        collecting = true
        methodBuffer = [line]

        // Continue collecting method lines
        while (++i < lines.size()) {
          def nextLine = lines[i]
          methodBuffer << nextLine
          if (nextLine.trim() == '}') {
            def method = methodBuffer.join("\n").trim()
            featureSteps[currentFeature] << method
            break
          }
        }
        collecting = false
        continue
      }
    }

    return featureSteps
  }
}
