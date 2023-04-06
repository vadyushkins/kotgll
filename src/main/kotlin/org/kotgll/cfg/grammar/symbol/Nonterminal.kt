package org.kotgll.cfg.grammar.symbol

import org.kotgll.cfg.grammar.Alternative
import java.util.*

open class Nonterminal(
    val name: String,
    val alternatives: MutableList<Alternative> = emptyList<Alternative>().toMutableList(),
) : Symbol, Iterable<Alternative> {
    override fun iterator(): Iterator<Alternative> = alternatives.iterator()

    override fun toString() = "Nonterminal($name)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Nonterminal) return false

        if (name != other.name) return false
        if (alternatives != other.alternatives) return false

        return true
    }

    override fun hashCode() = Objects.hash(name)

    fun addAlternative(alternative: Alternative) {
        alternatives.add(alternative)
        alternative.nonterminal = this
    }
}