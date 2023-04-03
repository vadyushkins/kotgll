package org.kotgll.rsm.grammar.symbol

class Literal(val literal: String) : Terminal {
    override fun match(pos: Int, input: String): String? {
        return if (input.startsWith(literal, pos)) literal else null
    }

    override fun toString() = literal
}