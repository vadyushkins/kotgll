package org.kotgll.rsm.grammar

import org.kotgll.rsm.grammar.symbol.Nonterminal
import java.util.*

class RSMNonterminalEdge(
    val nonterminal: Nonterminal,
    val head: RSMState,
) {
    override fun toString() = "RSMNonterminalEdge(nonterminal=$nonterminal, head=$head)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RSMNonterminalEdge) return false

        if (nonterminal != other.nonterminal) return false
        if (head != other.head) return false

        return true
    }

    override fun hashCode() = Objects.hash(nonterminal)


}