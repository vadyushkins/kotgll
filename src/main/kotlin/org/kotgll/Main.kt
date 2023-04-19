package org.kotgll

import java.io.File
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import org.kotgll.cfg.grammar.readCFGFromTXT
import org.kotgll.graph.readGraphFromCSV
import org.kotgll.rsm.grammar.readRSMFromTXT

enum class InputMode {
  STRING,
  GRAPH,
}

enum class GrammarMode {
  CFG,
  RSM,
}

enum class SPPFMode {
  ON,
  OFF,
}

fun main(args: Array<String>) {
  val parser = ArgParser("kotgll")
  val inputMode by
      parser
          .option(ArgType.Choice<InputMode>(), fullName = "input", description = "Input format")
          .required()
  val grammarMode by
      parser
          .option(
              ArgType.Choice<GrammarMode>(), fullName = "grammar", description = "Grammar format")
          .required()
  val sppfMode by
      parser
          .option(ArgType.Choice<SPPFMode>(), fullName = "sppf", description = "Sppf mode")
          .default(SPPFMode.ON)
  val pathToInput by
      parser
          .option(ArgType.String, fullName = "inputPath", description = "Path to input txt file")
          .required()
  val pathToGrammar by
      parser
          .option(
              ArgType.String, fullName = "grammarPath", description = "Path to grammar txt file")
          .required()
  val pathToOutput by
      parser
          .option(ArgType.String, fullName = "outputPath", description = "Path to output txt file")
          .required()

  parser.parse(args)

  if (inputMode == InputMode.STRING) {
    val input = File(pathToInput).readText()
    if (grammarMode == GrammarMode.CFG) {
      val grammar = readCFGFromTXT(pathToGrammar)
      if (sppfMode == SPPFMode.ON) {
        val result = org.kotgll.cfg.stringinput.withsppf.GLL(grammar, input).parse()
        File(pathToOutput).printWriter().use { out -> out.println(result != null) }
      } else if (sppfMode == SPPFMode.OFF) {
        val result = org.kotgll.cfg.stringinput.withoutsppf.GLL(grammar, input).parse()
        File(pathToOutput).printWriter().use { out -> out.println(result) }
      }
    } else if (grammarMode == GrammarMode.RSM) {
      val grammar = readRSMFromTXT(pathToGrammar)
      if (sppfMode == SPPFMode.ON) {
        val result = org.kotgll.rsm.stringinput.withsppf.GLL(grammar, input).parse()
        File(pathToOutput).printWriter().use { out -> out.println(result != null) }
      } else if (sppfMode == SPPFMode.OFF) {
        val result = org.kotgll.rsm.stringinput.withoutsppf.GLL(grammar, input).parse()
        File(pathToOutput).printWriter().use { out -> out.println(result) }
      }
    }
  } else if (inputMode == InputMode.GRAPH) {
    val graph = readGraphFromCSV(pathToInput)
    if (grammarMode == GrammarMode.CFG) {
      val grammar = readCFGFromTXT(pathToGrammar)
      if (sppfMode == SPPFMode.ON) {
        val result = org.kotgll.cfg.graphinput.withsppf.GLL(grammar, graph).parse()
        File(pathToOutput).printWriter().use { out ->
          result.keys.forEach { tail ->
            result[tail]!!.keys.forEach { head -> out.println("$tail $head") }
          }
        }
      } else if (sppfMode == SPPFMode.OFF) {
        val result = org.kotgll.cfg.graphinput.withoutsppf.GLL(grammar, graph).parse()
        File(pathToOutput).printWriter().use { out ->
          result.keys.forEach { tail ->
            result[tail]!!.forEach { head -> out.println("$tail $head") }
          }
        }
      }
    } else if (grammarMode == GrammarMode.RSM) {
      val grammar = readRSMFromTXT(pathToGrammar)
      if (sppfMode == SPPFMode.ON) {
        val result = org.kotgll.rsm.graphinput.withsppf.GLL(grammar, graph).parse()
        File(pathToOutput).printWriter().use { out ->
          result.keys.forEach { tail ->
            result[tail]!!.keys.forEach { head -> out.println("$tail $head") }
          }
        }
      } else if (sppfMode == SPPFMode.OFF) {
        val result = org.kotgll.rsm.graphinput.withoutsppf.GLL(grammar, graph).parse()
        File(pathToOutput).printWriter().use { out ->
          result.keys.forEach { tail ->
            result[tail]!!.forEach { head -> out.println("$tail $head") }
          }
        }
      }
    }
  }
}
