package org.kotgll.cfg.grammar

import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.grammar.symbol.Literal
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import java.io.InputStream

fun readCFGFromText(inputStream: InputStream): Nonterminal {
  var startNonterminal = Nonterminal("S")
  val nonterminals: HashMap<Nonterminal, Nonterminal> = HashMap()
  fun makeNonterminal(name: String): Nonterminal {
    val y = Nonterminal(name)
    if (!nonterminals.containsKey(y)) nonterminals[y] = y
    return nonterminals[y]!!
  }

  val startNonterminalRegex = """^StartNonterminal\("(?<value>.*)"\)$""".toRegex()
  val nonterminalRegex = """^Nonterminal\("(?<value>.*)"\)$""".toRegex()
  val charRegex = """^Char\('(?<value>.*)'\)$""".toRegex()
  val literalRegex = """^Literal\("(?<value>.*)"\)$""".toRegex()

  val reader = inputStream.bufferedReader()
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
        if (charRegex.matches(element)) {
          val elementValue = charRegex.matchEntire(element)!!.groups["value"]!!.value
          elements.add(Char(elementValue[0]))
        } else if (literalRegex.matches(element)) {
          val elementValue = literalRegex.matchEntire(element)!!.groups["value"]!!.value
          elements.add(Literal(elementValue))
        } else if (nonterminalRegex.matches(element)) {
          val elementValue = nonterminalRegex.matchEntire(element)!!.groups["value"]!!.value
          val tmpNonterminal = makeNonterminal(elementValue)
          elements.add(tmpNonterminal)
        }
      }
      nonterminals[nonterminal]!!.addAlternative(Alternative(elements.toList()))
    }
  }
  return nonterminals[startNonterminal]!!
}
