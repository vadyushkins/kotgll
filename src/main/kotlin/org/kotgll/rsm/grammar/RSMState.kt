package org.kotgll.rsm.grammar

import org.kotgll.rsm.grammar.symbol.Nonterminal
import java.util.*

class RSMState(
    val id: Int,
    val nonterminal: Nonterminal,
    val isStart: Boolean = false,
    val isFinal: Boolean = false,
) {
    val outgoingTerminalEdges: MutableList<RSMTerminalEdge> = mutableListOf()
    val outgoingNonterminalEdges: MutableList<RSMNonterminalEdge> = mutableListOf()

    override fun toString() = "RSMState(" +
            "id=$id, " +
            "nonterminal=$nonterminal, " +
            "isStart=$isStart, " +
            "isFinal=$isFinal)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RSMState) return false

        if (id != other.id) return false
        if (isStart != other.isStart) return false
        if (isFinal != other.isFinal) return false
        if (outgoingTerminalEdges != other.outgoingTerminalEdges) return false
        if (outgoingNonterminalEdges != other.outgoingNonterminalEdges) return false

        return true
    }

    override fun hashCode() = Objects.hash(
        id, isStart, isFinal, outgoingTerminalEdges, outgoingNonterminalEdges,
    )

    fun addTerminalEdge(edge: RSMTerminalEdge) {
        outgoingTerminalEdges.add(edge)
    }

    fun addNonterminalEdge(edge: RSMNonterminalEdge) {
        outgoingNonterminalEdges.add(edge)
    }
}