package org.kotgll.rsm.grammar

import org.kotgll.rsm.grammar.symbol.Terminal

class RSMTerminalEdge(val terminal: Terminal, val head: RSMState) : RSMEdge {
  override fun toString() = "RSMTerminalEdge(terminal=$terminal, head=$head)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RSMTerminalEdge) return false

    if (terminal != other.terminal) return false
    if (head != other.head) return false

    return true
  }

  val hashCode: Int = terminal.hashCode
  override fun hashCode() = hashCode
}
