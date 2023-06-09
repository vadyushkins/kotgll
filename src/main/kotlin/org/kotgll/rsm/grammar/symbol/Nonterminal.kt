package org.kotgll.rsm.grammar.symbol

import org.kotgll.rsm.grammar.RSMState

class Nonterminal(val name: String) : Symbol {
  lateinit var startState: RSMState
  override fun toString() = "Nonterminal($name)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Nonterminal) return false

    if (name != other.name) return false

    return true
  }

  val hashCode: Int = name.hashCode()
  override fun hashCode() = hashCode
}
