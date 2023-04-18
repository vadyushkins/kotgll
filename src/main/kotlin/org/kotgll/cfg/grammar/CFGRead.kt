package org.kotgll.cfg.grammar

import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import java.io.File

fun readCFGFromTXT(pathToTXT: String): Nonterminal {
  var startNonterminal = Nonterminal("S")
  val nonterminals: HashMap<Nonterminal, Nonterminal> = HashMap()
  fun makeNonterminal(name: String): Nonterminal {
    val y = Nonterminal(name)
    if (!nonterminals.containsKey(y)) nonterminals[y] = y
    return nonterminals[y]!!
  }

  val startNonterminalRegex = """^StartNonterminal\("(?<value>.*)"\)$""".toRegex()
  val terminalRegex = """^Terminal\("(?<value>.*)"\)$""".toRegex()
  val nonterminalRegex = """^Nonterminal\("(?<value>.*)"\)$""".toRegex()

  val reader = File(pathToTXT).inputStream().bufferedReader()
  while (true) {
    val line = reader.readLine() ?: break

    if (startNonterminalRegex.matches(line)) {
      startNonterminal =
          makeNonterminal(startNonterminalRegex.matchEntire(line)!!.groups["value"]!!.value)
    } else {
      val lineSplit = line.split(" ->", limit = 2)
      val alternativeNonterminal = lineSplit[0]
      val alternativeElements = lineSplit.elementAtOrNull(1) ?: ""

      val nonterminal =
          makeNonterminal(
              nonterminalRegex.matchEntire(alternativeNonterminal)!!.groups["value"]!!.value)

      val elements: MutableList<Symbol> = mutableListOf()
      for (element in alternativeElements.split(' ')) {
        if (terminalRegex.matches(element)) {
          val elementValue = terminalRegex.matchEntire(element)!!.groups["value"]!!.value
          elements.add(Terminal(elementValue))
        } else if (nonterminalRegex.matches(element)) {
          val elementValue = nonterminalRegex.matchEntire(element)!!.groups["value"]!!.value
          val tmpNonterminal = makeNonterminal(elementValue)
          elements.add(tmpNonterminal)
        }
      }
      nonterminals[nonterminal]!!.addAlternative(Alternative(elements))
    }
  }
  return nonterminals[startNonterminal]!!
}
