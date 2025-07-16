package com.mihael.mhipster

import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

@MGenerated
class Compressor {

	static final EXCLUDED_DIRS = ['node_modules', 'target']

	static String compress(String projectDir) {
		def sourceDir = new File(projectDir)
		def destination = "${projectDir}/../${projectDir.split("/").last()}.zip"
		def targetZip = new File(destination)
		zipDir(sourceDir, targetZip)
		return destination
	}

	static void zipDir(File dir, File zipFile) {
		zipFile.withOutputStream { fos ->
			new ZipOutputStream(fos).withCloseable { zipOut ->
				def addToZip
				addToZip = { File file, String entryName ->
					// Exclude unwanted directories
					if (file.isDirectory()) {
						if (EXCLUDED_DIRS.contains(file.name)) {
							return // skip this directory and its contents
						}
						file.listFiles().each { child ->
							addToZip(child, entryName + "/" + child.name)
						}
					} else {
						zipOut.putNextEntry(new ZipEntry(entryName))
						file.withInputStream { is ->
							is.transferTo(zipOut)
						}
						zipOut.closeEntry()
					}
				}
				addToZip(dir, dir.name)
			}
		}
	}
}
