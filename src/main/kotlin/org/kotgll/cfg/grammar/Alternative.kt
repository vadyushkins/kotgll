package org.kotgll.cfg.grammar

import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import java.util.*

class Alternative(val elements: List<Symbol>) {
    lateinit var nonterminal: Nonterminal

    override fun toString() = "Alternative($elements)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Alternative) return false

        if (elements != other.elements) return false
        if (nonterminal != other.nonterminal) return false

        return true
    }

    override fun hashCode() = Objects.hash(elements)
}