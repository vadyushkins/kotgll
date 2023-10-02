package org.kotgll.benchmarks

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import org.kotgll.cfg.grammar.readCFGFromTXT
import org.kotgll.graph.readGraphFromCSV
import org.kotgll.graph.readStartNodesFromCSV
import org.kotgll.rsm.grammar.readRSMFromTXT
import java.io.File
import kotlin.system.measureNanoTime

fun getResultPath(
    pathToOutput: String,
    graph: String,
    grammarMode: String = "cfg",
    grammarName: String,
    sppfMode: String,
): String {
  return pathToOutput +
      (if (pathToOutput.endsWith("/")) "" else "/") +
      "${graph}_${grammarMode}_${grammarName}_${sppfMode}.csv"
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
  val parser = ArgParser("kotgll.benchmarks")
  val grammarMode by
      parser
          .option(
              ArgType.Choice<GrammarMode>(), fullName = "grammar", description = "Grammar format")
          .required()
  val sppfMode by
      parser
          .option(ArgType.Choice<SPPFMode>(), fullName = "sppf", description = "Sppf mode")
          .default(SPPFMode.ON)
  val pathToGraphs by
      parser
          .option(
              ArgType.String, fullName = "graphsPath", description = "Path to folder with graphs")
          .required()
  val pathToStartNodes by
      parser
          .option(
              ArgType.String,
              fullName = "startNodesPath",
              description = "Path to folder with start nodes for each graph")
          .default("")
  val pathToGrammar by
      parser
          .option(
              ArgType.String, fullName = "grammarPath", description = "Path to grammar txt file")
          .required()
  val pathToOutput by
      parser
          .option(
              ArgType.String, fullName = "outputPath", description = "Path to folder with results")
          .required()
  val warmUpRounds by
      parser
          .option(ArgType.Int, fullName = "warmUpRounds", description = "Number of warm-up rounds")
          .default(3)
  val benchmarksRounds by
      parser
          .option(
              ArgType.Int, fullName = "benchmarkRounds", description = "Number of benchmark rounds")
          .default(10)
  val chunkSize by
      parser
          .option(ArgType.Int, fullName = "chunkSize", description = "Start nodes chunk size")
          .default(10)

  parser.parse(args)

  if (grammarMode == GrammarMode.CFG) {
    if (sppfMode == SPPFMode.ON) {
      runCFGWithSPPF(pathToGraphs,
        pathToStartNodes, pathToGrammar, pathToOutput, warmUpRounds, benchmarksRounds, chunkSize)
    } else if (sppfMode == SPPFMode.OFF) {
      runCFGWithoutSPPF(
          pathToGraphs,
          pathToStartNodes,
          pathToGrammar,
          pathToOutput,
          warmUpRounds,
          benchmarksRounds,
          chunkSize)
    }
  } else if (grammarMode == GrammarMode.RSM) {
    if (sppfMode == SPPFMode.ON) {
      runRSMWithSPPF(pathToGraphs,
        pathToStartNodes, pathToGrammar, pathToOutput, warmUpRounds, benchmarksRounds, chunkSize)
    } else if (sppfMode == SPPFMode.OFF) {
      runRSMWithoutSPPF(pathToGraphs, pathToStartNodes, pathToGrammar, pathToOutput, warmUpRounds, benchmarksRounds,
        chunkSize)
    }
  }
}

fun runCFGWithoutSPPF(
    pathToGraphs: String,
    pathToStartNodes: String,
    pathToCFG: String,
    pathToOutput: String,
    warmUpRounds: Int,
    benchmarkRounds: Int,
    chunkSize: Int
) {
  val cfg = readCFGFromTXT(pathToCFG)
  val cfgName = File(pathToCFG).nameWithoutExtension
  File(pathToGraphs)
      .walk()
      .filter { it.isFile }
      .forEach { graphPath ->
        val graphName = graphPath.nameWithoutExtension
        println("start:: $graphName")
        val graph = readGraphFromCSV(graphPath.path)
        val startNodes = readStartNodesFromCSV("$pathToStartNodes/$graphName.csv")

        for (chunkStart in 0..<startNodes.size step chunkSize) {
          val chunkEnd = kotlin.math.min(chunkStart + chunkSize, startNodes.size)

          val resultPath = getResultPath(pathToOutput, graphName, "cfg", cfgName, "without_sppf")
          File(resultPath).writeText("")

          for (warmUp in 1..warmUpRounds) {
            var result: HashMap<Int, HashSet<Int>>
            val elapsed = measureNanoTime {
              result =
                  org.kotgll.cfg.graphinput.withoutsppf
                      .GLL(cfg, graph.subList(chunkStart, chunkEnd))
                      .parse()
            }
            val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

            var number = 0
            result.keys.forEach { key -> number += result[key]!!.size }

            println("warmup:: $graphName $cfgName ${number} $elapsedSeconds")
          }

          for (benchmarkAttempt in 1..benchmarkRounds) {
            var result: HashMap<Int, HashSet<Int>>
            val elapsed = measureNanoTime {
              result =
                  org.kotgll.cfg.graphinput.withoutsppf
                      .GLL(cfg, graph.subList(chunkStart, chunkEnd))
                      .parse()
            }
            val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

            var number = 0
            result.keys.forEach { key -> number += result[key]!!.size }

            println("benchmark:: $graphName $cfgName ${number} $elapsedSeconds")
            File(resultPath).appendText(elapsed.toString() + "\n")
          }
        }
      }
}

fun runCFGWithSPPF(
  pathToGraphs: String,
  pathToStartNodes: String,
  pathToCFG: String,
  pathToOutput: String,
  warmUpRounds: Int,
  benchmarkRounds: Int,
  chunkSize: Int
) {
  val cfg = readCFGFromTXT(pathToCFG)
  val cfgName = File(pathToCFG).nameWithoutExtension
  File(pathToGraphs)
      .walk()
      .filter { it.isFile }
      .forEach { graphPath ->
        val graphName = graphPath.nameWithoutExtension
        println("start:: $graphName")
        val graph = readGraphFromCSV(graphPath.path)
        val startNodes = readStartNodesFromCSV("$pathToStartNodes/$graphName.csv")

        for (chunkStart in 0..<startNodes.size step chunkSize) {
          val chunkEnd = kotlin.math.min(chunkStart + chunkSize, startNodes.size)

            val resultPath = getResultPath(pathToOutput, graphName, "cfg", cfgName, "with_sppf")
            File(resultPath).writeText("")

            for (warmUp in 1..warmUpRounds) {
              var result: HashMap<Int, HashMap<Int, org.kotgll.cfg.graphinput.withsppf.sppf.SPPFNode>>
              val elapsed = measureNanoTime {
                result = org.kotgll.cfg.graphinput.withsppf.GLL(cfg, graph.subList(chunkStart, chunkEnd)).parse()
              }
              val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

              var number = 0
              result.keys.forEach { key -> number += result[key]!!.keys.size }

              println("warmup:: $graphName $cfgName ${number} $elapsedSeconds")
            }

            for (benchmarkAttempt in 1..benchmarkRounds) {
              var result: HashMap<Int, HashMap<Int, org.kotgll.cfg.graphinput.withsppf.sppf.SPPFNode>>
              val elapsed = measureNanoTime {
                result = org.kotgll.cfg.graphinput.withsppf.GLL(cfg, graph.subList(chunkStart, chunkEnd)).parse()
              }
              val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

              var number = 0
              result.keys.forEach { key -> number += result[key]!!.keys.size }

              println("benchmark:: $graphName $cfgName ${number} $elapsedSeconds")
              File(resultPath).appendText(elapsed.toString() + "\n")
            }
          }
      }
}

fun runRSMWithoutSPPF(
    pathToGraphs: String,
    pathToStartNodes: String,
    pathToRSM: String,
    pathToOutput: String,
    warmUpRounds: Int,
    benchmarkRounds: Int,
    chunkSize: Int,
) {
  val rsm = readRSMFromTXT(pathToRSM)
  val rsmName = File(pathToRSM).nameWithoutExtension
  File(pathToGraphs)
      .walk()
      .filter { it.isFile }
      .forEach { graphPath ->
        val graphName = graphPath.nameWithoutExtension
        println("start:: $graphName")
        val graph = readGraphFromCSV(graphPath.path)
        val startNodes = readStartNodesFromCSV("$pathToStartNodes/$graphName.csv")

        for (chunkStart in 0..<startNodes.size step chunkSize) {
          val chunkEnd = kotlin.math.min(chunkStart + chunkSize, startNodes.size)

          val resultPath = getResultPath(pathToOutput, graphName, "rsm", rsmName, "without_sppf")
          File(resultPath).writeText("")

          for (warmUp in 1..warmUpRounds) {
            var result: HashMap<Int, HashSet<Int>>
            val elapsed = measureNanoTime {
              result = org.kotgll.rsm.graphinput.withoutsppf.GLL(rsm, graph.subList(chunkStart, chunkEnd)).parse()
            }
            val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

            var number = 0
            result.keys.forEach { key -> number += result[key]!!.size }

            println("warmup:: $graphName $rsmName ${number} $elapsedSeconds")
          }

          for (benchmarkAttempt in 1..benchmarkRounds) {
            var result: HashMap<Int, HashSet<Int>>
            val elapsed = measureNanoTime {
              result = org.kotgll.rsm.graphinput.withoutsppf.GLL(rsm, graph.subList(chunkStart, chunkEnd)).parse()
            }
            val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

            var number = 0
            result.keys.forEach { key -> number += result[key]!!.size }

            println("benchmark:: $graphName $rsmName ${number} $elapsedSeconds")
            File(resultPath).appendText(elapsed.toString() + "\n")
          }
        }
      }
}

fun runRSMWithSPPF(
  pathToGraphs: String,
  pathToStartNodes: String,
  pathToRSM: String,
  pathToOutput: String,
  warmUpRounds: Int,
  benchmarkRounds: Int,
  chunkSize: Int,
) {
  val rsm = readRSMFromTXT(pathToRSM)
  val rsmName = File(pathToRSM).nameWithoutExtension
  File(pathToGraphs)
      .walk()
      .filter { it.isFile }
      .forEach { graphPath ->
        val graphName = graphPath.nameWithoutExtension
        println("start:: $graphName")
        val graph = readGraphFromCSV(graphPath.path)
        val startNodes = readStartNodesFromCSV("$pathToStartNodes/$graphName.csv")

        for (chunkStart in 0..<startNodes.size step chunkSize) {
          val chunkEnd = kotlin.math.min(chunkStart + chunkSize, startNodes.size)

            val resultPath = getResultPath(pathToOutput, graphName, "rsm", rsmName, "with_sppf")
            File(resultPath).writeText("")

            for (warmUp in 1..warmUpRounds) {
              var result: HashMap<Int, HashMap<Int, org.kotgll.rsm.graphinput.withsppf.sppf.SPPFNode>>
              val elapsed = measureNanoTime {
                result = org.kotgll.rsm.graphinput.withsppf.GLL(rsm, graph.subList(chunkStart, chunkEnd)).parse()
              }
              val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

              var number = 0
              result.keys.forEach { key -> number += result[key]!!.keys.size }

              println("warmup:: $graphName $rsmName ${number} $elapsedSeconds")
            }

            for (benchmarkAttempt in 1..benchmarkRounds) {
              var result: HashMap<Int, HashMap<Int, org.kotgll.rsm.graphinput.withsppf.sppf.SPPFNode>>
              val elapsed = measureNanoTime {
                result = org.kotgll.rsm.graphinput.withsppf.GLL(rsm, graph.subList(chunkStart, chunkEnd)).parse()
              }
              val elapsedSeconds = elapsed.toDouble() / 1_000_000_000.0

              var number = 0
              result.keys.forEach { key -> number += result[key]!!.keys.size }

              println("benchmark:: $graphName $rsmName ${number} $elapsedSeconds")
              File(resultPath).appendText(elapsed.toString() + "\n")
            }
          }
      }
}
