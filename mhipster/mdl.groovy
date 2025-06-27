import java.nio.file.*
import java.util.regex.*
import groovy.transform.Field

//@Field JAVA_BASE_PATH = "../../src/main/java/com/mihael/jhip/domain"
@Field JAVA_BASE_PATH = "fake-domain"
@Field FIELD_PREFIXES = ['part', 'whole', 'assoc', 'parent', 'child']
@Field CASCADE_REMOVE = 'CascadeType.REMOVE'
@Field CASCADE_PERSIST = 'CascadeType.PERSIST'

// Extract lines after "relationship"
List<String> getLinesAfterRelationship(File file) {
    def lines = file.readLines()
    def afterLines = []
    lines.eachWithIndex { line, i ->
        if (line.contains('relationship') && i + 1 < lines.size()) {
            afterLines << lines[i + 1].trim()
        }
    }
    return afterLines
}

def extractEntitiesWithCascades(List<String> lines) {
    def deletes = []
    def persists = []

    lines.each { line ->
        def (left, right) = line.split(" to ")
        def name1 = left.split("\\{")[0].trim().tokenize().last()
        def name2 = right.split("\\{")[0].trim().tokenize().last()
        def d1 = left.contains("OnDelete")
        def d2 = right.contains("OnDelete")
        def p1 = left.contains("OnPersist")
        def p2 = right.contains("OnPersist")
        deletes << [(name1): d1, (name2): d2]
        persists << [(name1): p1, (name2): p2]
    }

    return [deletes, persists]
}

def processCascades(List<Map<String, Boolean>> cascadeList, String cascadeType) {
    cascadeList.each { relMap ->
        def source = relMap.find { it.value }?.key
        if (!source) return
        relMap.keySet().findAll { it != source }.each { target ->
            def javaFile = new File("${JAVA_BASE_PATH}/${source}.java")
            processJavaFileForCascade(javaFile, target, cascadeType)
        }
    }
}

def processJavaFileForCascade(File javaFile, String target, String cascadeType) {
    if (!javaFile.exists()) {
        println "❌ File not found: $javaFile"
        return
    }

    def lines = javaFile.readLines()
    def modified = false
    def targetFieldNames = FIELD_PREFIXES.collect { it + target }

    def jpaPattern = ~/@(OneToOne|OneToMany|ManyToOne|ManyToMany)\s*(\(([^)]*)\))?/

    lines.eachWithIndex { line, idx ->
        if (targetFieldNames.any { line.contains(it) } && (line.contains(";") || line.contains("="))) {
            def i = idx - 1
            while (i >= 0 && lines[i].trim().startsWith("@")) {
                def annLine = lines[i]
                def matcher = jpaPattern.matcher(annLine)
                if (matcher.find()) {
                    def params = matcher.group(3) ?: ""
                    if (params.contains("cascade")) {
                        if (!params.contains(cascadeType)) {
                            def newParams = params.replaceAll(/cascade\s*=\s*\{([^\}]*)\}/) { all, group ->
                                "cascade = { ${group.trim()}, ${cascadeType} }"
                            }
                            lines[i] = "@${matcher.group(1)}(${newParams})"
                            modified = true
                        }
                    } else {
                        def newParams = params ? "${params}, cascade = { ${cascadeType} }" : "cascade = { ${cascadeType} }"
                        lines[i] = "@${matcher.group(1)}(${newParams})"
                        modified = true
                    }
                }
                i--
            }
        }
    }

    if (modified) {
        javaFile.text = lines.join("\n")
        println "✅ Modified $javaFile"
    } else {
        println "⚠️ No applicable field updated in $javaFile"
    }
}

def parseEntityMethods(File jdlFile) {
    def content = jdlFile.text

    def entityPattern = Pattern.compile(
        /entity\s+(\w+)\s*\{.*?\/\*\s*methods\s*(.*?)\*\//,
        Pattern.DOTALL
    )

    def matcher = entityPattern.matcher(content)
    def entityMap = [:]

    while (matcher.find()) {
        def entity = matcher.group(1)
        def methods = matcher.group(2).readLines().collect { it.trim() }.findAll { it }
        entityMap[entity] = methods
    }

    return entityMap
}

def insertMethodStubs(File javaFile, List<String> methodLines) {
    if (!javaFile.exists()) {
        println "⚠️ Java file not found: $javaFile"
        return
    }

    def lines = javaFile.readLines()
    def insertIndex = lines.lastIndexOf("}")

    if (insertIndex > -1) {
        def indent = "    "
        def stubs = methodLines.collect { indent + it.replaceAll(/;$/, "") + " {}\n" }
        lines.addAll(insertIndex, stubs + ["\n"])
        javaFile.text = lines.join("\n")
        println "✅ Updated $javaFile with ${methodLines.size()} method(s)."
    }
}

// --------------------- MAIN ------------------------

if (args.length != 1) {
    println "Usage: groovy JdlMod.groovy <path_to_jdl_file>"
    System.exit(1)
}

def jdlFile = new File(args[0])
if (!jdlFile.exists()) {
    println "JDL file not found."
    System.exit(1)
}

try {
    def relLines = getLinesAfterRelationship(jdlFile)
    def (deletes, persists) = extractEntitiesWithCascades(relLines)
    println "deletes" + deletes
    println "persists" + persists

    println "🔁 Processing deletes..."
    processCascades(deletes, CASCADE_REMOVE)

    println "🔁 Processing persists..."
    processCascades(persists, CASCADE_PERSIST)

    def entityMethodMap = parseEntityMethods(jdlFile)
    entityMethodMap.each { entity, methods ->
        def javaFile = new File("${JAVA_BASE_PATH}/${entity}.java")
        insertMethodStubs(javaFile, methods)
    }
} catch (Exception e) {
    println "❌ Error: ${e.message}"
    e.printStackTrace()
    System.exit(1)
}
