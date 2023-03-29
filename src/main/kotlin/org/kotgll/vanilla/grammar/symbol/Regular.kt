package org.kotgll.vanilla.grammar.symbol

import java.util.*

open class Regular(
    val symbol: Symbol,
    val suffix: String,
) : Nonterminal(symbol.toString() + suffix) {
    override fun toString() = "$symbol$suffix"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Regular) return false
        if (!super.equals(other)) return false

        if (symbol != other.symbol) return false
        if (suffix != other.suffix) return false

        return true
    }

    override fun hashCode() = Objects.hash(symbol, suffix)
}