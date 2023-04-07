package org.kotgll.cfg.grammar.symbol

class Literal(val literal: String) : Terminal {
    val hashCode: Int = literal.hashCode()
    override fun match(pos: Int, input: String): String? {
        return if (input.startsWith(literal, pos)) literal else null
    }

    override fun toString() = "Literal($literal)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Literal) return false

        if (literal != other.literal) return false

        return true
    }

    override fun hashCode() = hashCode


}