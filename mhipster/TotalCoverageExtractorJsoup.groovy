import groovy.json.JsonOutput
@Grab('org.jsoup:jsoup:1.17.2')
import org.jsoup.Jsoup

class TotalCoverageExtractorJsoup {

  static void main(String[] args) {
    extractTotalsToJson("/home/mihael/Projects/MHipster2/target/site/jacoco-mit-runtime/index.html", "coverage-summary.json")
  }

  static void extractTotalsToJson(String htmlFilePath, String outputJsonPath) {
    def doc = Jsoup.parse(new File(htmlFilePath), "UTF-8")
    def totalRow = doc.select("tr").find { row ->
      def firstCell = row.selectFirst("td")
      firstCell && firstCell.text().trim() == "Total"
    }


    if (!totalRow) {
      println "❌ 'Total' row not found."
      return
    }

    def cells = totalRow.select("td").drop(1) // Skip "Total" cell

    if (cells.size() < 12) {
      println "❌ Not enough data cells in 'Total' row."
      return
    }

    def (missedInstr, totalInstr) = parseRatio(cells[0].text())
    def (missedBranches, totalBranches) = parseRatio(cells[2].text())

    def result = [
      missedInstructions: missedInstr,
      instructions      : totalInstr,
      missedBranches    : missedBranches,
      branches          : totalBranches,
      missedLines       : toInt(cells[6].text()),
      lines             : toInt(cells[7].text()),
      missedMethods     : toInt(cells[8].text()),
      methods           : toInt(cells[9].text()),
      missedClasses     : toInt(cells[10].text()),
      classes           : toInt(cells[11].text())
    ]

    new File(outputJsonPath).text = JsonOutput.prettyPrint(JsonOutput.toJson(result))
    println "✅ Coverage extracted to ${outputJsonPath}"
  }

  private static List<Integer> parseRatio(String text) {
    def matcher = text =~ /([\d,]+)\s+of\s+([\d,]+)/
    return matcher ? [toInt(matcher[0][1]), toInt(matcher[0][2])] : [0, 0]
  }

  private static int toInt(String text) {
    return text.replaceAll(",", "").toInteger()
  }
}
