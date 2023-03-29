package org.kotgll.rsm.grammar

import org.kotgll.rsm.grammar.symbol.Nonterminal
import java.util.*

class RSMState(
    val id: Int,
    val isStartFor: List<Nonterminal> = emptyList(),
    val isFinalFor: List<Nonterminal> = emptyList(),
) {
    val outgoingTerminalEdges: MutableList<RSMTerminalEdge> = mutableListOf()
    val outgoingNonterminalEdges: MutableList<RSMNonterminalEdge> = mutableListOf()

    override fun toString() = "RSMState(" +
            "id=$id, " +
            "isStartFor=$isStartFor, " +
            "isFinalFor=$isFinalFor, " +
            "outgoingTerminalEdges=$outgoingTerminalEdges, " +
            "outgoingNonterminalEdges=$outgoingNonterminalEdges" +
            ")"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RSMState) return false

        if (id != other.id) return false
        if (isStartFor != other.isStartFor) return false
        if (isFinalFor != other.isFinalFor) return false
        if (outgoingTerminalEdges != other.outgoingTerminalEdges) return false
        if (outgoingNonterminalEdges != other.outgoingNonterminalEdges) return false

        return true
    }

    override fun hashCode() = Objects.hash(
        id, isStartFor, isFinalFor, outgoingTerminalEdges, outgoingNonterminalEdges,
        )

    fun addTerminalEdge(edge: RSMTerminalEdge) {
        outgoingTerminalEdges.add(edge)
    }

    fun addNonterminalEdge(edge: RSMNonterminalEdge) {
        outgoingNonterminalEdges.add(edge)
    }
}