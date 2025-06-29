if (args.length != 2) {
  println "Usage: groovy modifyRetentionPolicy.groovy <path_to_java_file> <SOURCE|RUNTIME>"
  System.exit(1)
}

def javaFilePath = args[0]
def newPolicy = args[1].toUpperCase()

if (!['SOURCE', 'RUNTIME'].contains(newPolicy)) {
  println "Error: Retention policy must be either SOURCE or RUNTIME"
  System.exit(1)
}

def javaFile = new File(javaFilePath)
if (!javaFile.exists()) {
  println "Error: File not found - $javaFilePath"
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
  println "Modified retention policy in $javaFilePath to $newPolicy"
} else {
  println "No @Retention annotation found in $javaFilePath"
}
