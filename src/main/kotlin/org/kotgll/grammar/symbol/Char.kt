package org.kotgll.grammar.symbol

import org.kotgll.GLL
import kotlin.Char

class Char(val char: Char) : Terminal {
    override fun match(pos: Int, driver: GLL): String? {
        if (driver.input[pos] == char) {
            return char + ""
        }
        return null
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