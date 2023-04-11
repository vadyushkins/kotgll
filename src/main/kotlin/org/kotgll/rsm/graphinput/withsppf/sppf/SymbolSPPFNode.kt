package org.kotgll.rsm.graphinput.withsppf.sppf

import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Symbol
import org.kotgll.rsm.graphinput.graph.GraphNode
import java.util.*

class SymbolSPPFNode(leftExtent: GraphNode, rightExtent: GraphNode, val symbol: Nonterminal) :
    ParentSPPFNode(leftExtent, rightExtent) {
  override fun toString() =
      "SymbolSPPFNode(leftExtent=$leftExtent, rightExtent=$rightExtent, symbol=$symbol)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SymbolSPPFNode) return false
    if (!super.equals(other)) return false

    if (symbol != other.symbol) return false

    return true
  }

  override val hashCode: Int = Objects.hash(leftExtent, rightExtent, symbol)
  override fun hashCode() = hashCode

  override fun hasSymbol(symbol: Symbol) = this.symbol == symbol
}
