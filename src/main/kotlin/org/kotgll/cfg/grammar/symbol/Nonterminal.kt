package org.kotgll.cfg.grammar.symbol

import org.kotgll.cfg.grammar.Alternative

open class Nonterminal(
    val name: String,
    val alternatives: ArrayList<Alternative> = ArrayList(),
) : Symbol {
  override fun toString() = "Nonterminal($name)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Nonterminal) return false

    if (name != other.name) return false

    return true
  }

  open val hashCode: Int = name.hashCode()
  override fun hashCode() = hashCode

  fun addAlternative(alternative: Alternative) {
    alternatives.add(alternative)
    alternative.nonterminal = this
  }
}
