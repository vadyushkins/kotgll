package org.kotgll.grammar.symbol

import org.kotgll.GLL

class Literal(val literal: String) : Terminal {
    override fun match(pos: Int, driver: GLL): String? {
        if (driver.input.startsWith(literal, pos)) {
            return literal
        }
        return null
    }

    override fun toString() = literal
}