package org.kotgll.sppf

import org.kotgll.grammar.symbol.Nonterminal
import org.kotgll.grammar.symbol.Symbol
import java.util.*

class SymbolSPPFNode(
    leftExtent: Int,
    rightExtent: Int,
    val symbol: Nonterminal,
) : ParentSPPFNode(leftExtent, rightExtent) {
    override fun toString() = "SymbolSPPFNode(" +
            "leftExtent=$leftExtent," +
            "rightExtent=$rightExtent, " +
            "kids=$kids, " +
            "symbol=$symbol)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SymbolSPPFNode) return false
        if (!super.equals(other)) return false

        if (symbol != other.symbol) return false

        return true
    }

    override fun hashCode() = Objects.hash(leftExtent, rightExtent, kids, symbol)

    override fun hasSymbol(symbol: Symbol) = this.symbol == symbol
}