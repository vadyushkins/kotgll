package org.kotgll.rsm.grammar

import org.kotgll.rsm.grammar.symbol.Nonterminal

class RSMState(
    val id: Int,
    val nonterminal: Nonterminal,
    val isStart: Boolean = false,
    val isFinal: Boolean = false
) {
  val outgoingTerminalEdges: ArrayList<RSMTerminalEdge> = ArrayList()
  val outgoingNonterminalEdges: ArrayList<RSMNonterminalEdge> = ArrayList()

  override fun toString() =
      "RSMState(id=$id, nonterminal=$nonterminal, isStart=$isStart, isFinal=$isFinal)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RSMState) return false

    if (id != other.id) return false

    return true
  }

  val hashCode: Int = id
  override fun hashCode() = hashCode

  fun addTerminalEdge(edge: RSMTerminalEdge) {
    if (!outgoingTerminalEdges.contains(edge)) outgoingTerminalEdges.add(edge)
  }

  fun addNonterminalEdge(edge: RSMNonterminalEdge) {
    if (!outgoingNonterminalEdges.contains(edge)) outgoingNonterminalEdges.add(edge)
  }
}
