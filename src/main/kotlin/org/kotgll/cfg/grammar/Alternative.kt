package org.kotgll.cfg.grammar

import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol

class Alternative(val elements: ArrayList<Symbol>) {
  constructor(elements: List<Symbol>) : this(ArrayList(elements))

  lateinit var nonterminal: Nonterminal

  override fun toString() = "Alternative($elements)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Alternative) return false

    if (elements != other.elements) return false
    if (nonterminal != other.nonterminal) return false

    return true
  }

  val hashCode = elements.hashCode()
  override fun hashCode() = hashCode
}
