package org.kotgll.rsm.grammar.symbol

import java.util.*

class Nonterminal(val name: String) : Symbol {
    override fun toString() = "Nonterminal(name='$name')"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Nonterminal) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = Objects.hash(name)
}