package org.kotgll.grammar.symbol

import kotlin.Char

class Char(val char: Char) : Terminal {
    override fun match(pos: Int, input: String): String? {
        return if (input[pos] == char) char + "" else null
    }

    override fun toString() = "Char(char='$char')"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is org.kotgll.grammar.symbol.Char) return false

        if (char != other.char) return false

        return true
    }

    override fun hashCode() = char.hashCode()


}