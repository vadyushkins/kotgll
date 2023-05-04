package org.kotgll.cfg.grammar

import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Terminal
import java.io.File

fun writeCFGToTXT(cfg: Nonterminal, pathToTXT: String) {
  val nonterminals: ArrayList<Nonterminal> = ArrayList()
  val queue: ArrayDeque<Nonterminal> = ArrayDeque(listOf(cfg))
  while (!queue.isEmpty()) {
    val nonterminal = queue.removeFirst()
    if (!nonterminals.contains(nonterminal)) nonterminals.add(nonterminal)
    for (alternative in nonterminal.alternatives) {
      for (symbol in alternative.elements) {
        if (symbol is Nonterminal) {
          if (!nonterminals.contains(symbol)) queue.addLast(symbol)
        }
      }
    }
  }

  File(pathToTXT).printWriter().use { out ->
    out.println("""StartNonterminal("${cfg.name}")""")
    nonterminals.forEach { nonterminal ->
      nonterminal.alternatives.forEach { alternative ->
        var alternativeString = """Nonterminal("${alternative.nonterminal.name}")"""
        alternativeString += " ->"
        alternative.elements.forEach { element ->
          alternativeString += " "
          if (element is Terminal) {
            alternativeString += """Terminal("${element.value}")"""
          } else if (element is Nonterminal) {
            alternativeString += """Nonterminal("${element.name}")"""
          }
        }
        out.println(alternativeString)
      }
    }
  }
}
