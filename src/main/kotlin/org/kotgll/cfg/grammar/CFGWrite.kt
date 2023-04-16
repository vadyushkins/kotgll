package org.kotgll.cfg.grammar

import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.grammar.symbol.Literal
import org.kotgll.cfg.grammar.symbol.Nonterminal
import java.io.File

fun writeCFGToTXT(cfg: Nonterminal, pathToTXT: String) {
  val alternatives: MutableList<Alternative> = mutableListOf()
  val nonterminals: HashSet<Nonterminal> = HashSet()
  val queue: ArrayDeque<Nonterminal> = ArrayDeque(listOf(cfg))
  while (!queue.isEmpty()) {
    val nonterminal = queue.removeFirst()
    nonterminals.add(nonterminal)
    for (alternative in nonterminal.alternatives) {
      if (!alternatives.contains(alternative)) alternatives.add(alternative)
      for (symbol in alternative.elements) {
        if (symbol is Nonterminal) {
          if (!nonterminals.contains(symbol)) {
            queue.addLast(symbol)
          }
        }
      }
    }
  }

  File(pathToTXT).printWriter().use { out ->
    out.println("""StartNonterminal("${cfg.name}")""")
    alternatives.forEach { alternative ->
      var alternativeString = """Nonterminal("${alternative.nonterminal.name}")"""
      alternativeString += " ->"
      alternative.elements.forEach { element ->
        alternativeString += " "
        if (element is Char) {
          alternativeString += """Char('${element.char}')"""
        } else if (element is Literal) {
          alternativeString += """Literal("${element.literal}")"""
        } else if (element is Nonterminal) {
          alternativeString += """Nonterminal("${element.name}")"""
        }
      }
      out.println(alternativeString)
    }
  }
}
