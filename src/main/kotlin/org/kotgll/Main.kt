package org.kotgll

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import org.kotgll.cfg.grammar.readCFGFromText
import org.kotgll.rsm.grammar.readRSMFromTXT
import readGraphFromCSV
import java.io.File

enum class SPPFMode {
  ON,
  OFF
}

fun main(args: Array<String>) {
  val parser = ArgParser("cli")
  val pathToCFG by
      parser
          .option(ArgType.String, fullName = "cfg", description = "Path to CFG txt file")
          .default("")
  val pathToRSM by
      parser
          .option(ArgType.String, fullName = "rsm", description = "Path to RSM txt file")
          .default("")
  val pathToGraph by
      parser
          .option(ArgType.String, fullName = "graph", description = "Path to graph csv file")
          .required()
  val pathToResult by
      parser
          .option(ArgType.String, fullName = "result", description = "Path to results file")
          .required()
  val sppfMode by
      parser
          .option(ArgType.Choice<SPPFMode>(), fullName = "sppf", description = "Turn on SPPF mode")
          .default(SPPFMode.ON)

  parser.parse(args)

  val graph = readGraphFromCSV(File(pathToGraph).inputStream())

  if (sppfMode == SPPFMode.ON) {
    if (pathToCFG != "") {
      val grammarCFG = readCFGFromText(File(pathToCFG).inputStream())
      val result = org.kotgll.cfg.graphinput.withsppf.GLL(grammarCFG, graph).parse()
      if (result != null) {
        File(pathToResult).printWriter().use { out ->
          result.keys.forEach { tail ->
            result[tail]?.keys!!.forEach { head -> out.println("$tail $head") }
          }
        }
      }
    } else if (pathToRSM != "") {
      val grammarRSM = readRSMFromTXT(pathToRSM)
      val result = org.kotgll.rsm.graphinput.withsppf.GLL(grammarRSM, graph).parse()
      if (result != null) {
        File(pathToResult).printWriter().use { out ->
          result.keys.forEach { tail ->
            result[tail]?.keys!!.forEach { head -> out.println("$tail $head") }
          }
        }
      }
    }
  } else if (sppfMode == SPPFMode.OFF) {
    if (pathToCFG != "") {
      val grammarCFG = readCFGFromText(File(pathToCFG).inputStream())
      val result = org.kotgll.cfg.graphinput.withoutsppf.GLL(grammarCFG, graph).parse()
      File(pathToResult).printWriter().use { out ->
        result.keys.forEach { tail ->
          result[tail]?.forEach { head -> out.println("$tail $head") }
        }
      }
    } else if (pathToRSM != "") {
      val grammarRSM = readRSMFromTXT(pathToRSM)
      val result = org.kotgll.rsm.graphinput.withoutsppf.GLL(grammarRSM, graph).parse()
      File(pathToResult).printWriter().use { out ->
        result.keys.forEach { tail ->
          result[tail]?.forEach { head -> out.println("$tail $head") }
        }
      }
    }
  }
}
