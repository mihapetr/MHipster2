package com.mihael.mhipster

import com.mihael.mhipster.domain.TestReport
import groovy.json.JsonOutput
import org.jsoup.Jsoup

@MGenerated
class ReportParser {

  	static TestReport html2TestReport(String reportContent) {

		def doc = Jsoup.parse(reportContent)
    	def totalRow = doc.select("tr").find { row ->
      	def firstCell = row.selectFirst("td")
      	firstCell && firstCell.text().trim() == "Total"
    	}

		def cells = totalRow.select("td").drop(1) // Skip "Total" cell

		def (missedInstr, totalInstr) = parseRatio(cells[0].text())
		def (missedBranches, totalBranches) = parseRatio(cells[2].text())

		return new TestReport()
			.missedInstructions(missedInstr)
			.instructions(totalInstr)
			.missedBranches(missedBranches)
			.branches(totalBranches)
			.missedLines(toInt(cells[6].text()))
			.lines(toInt(cells[7].text()))
			.missedMethods(toInt(cells[8].text()))
			.methods(toInt(cells[9].text()))
			.missedClasses(toInt(cells[10].text()))
			.classes(toInt(cells[11].text()))
	}

  	private static List<Integer> parseRatio(String text) {
    	def matcher = text =~ /([\d,]+)\s+of\s+([\d,]+)/
		return matcher ? [toInt(matcher[0][1]), toInt(matcher[0][2])] : [0, 0]
  	}

  	private static int toInt(String text) {
		return text.replaceAll(",", "").toInteger()
  	}
}
