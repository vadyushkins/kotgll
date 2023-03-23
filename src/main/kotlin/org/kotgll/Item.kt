package org.kotgll

import org.kotgll.sppf.SPPFNode
import java.util.*

class Item(val alternative: Alternative, val dot: Int) : Parser {
    override fun parse(pos: Int, cu: GSSNode, cn: SPPFNode?, ctx: GLL) {
        alternative.parseAt(dot, pos, cu, cn, ctx)
    }

    fun isAtEnd() = dot == alternative.elements.size

    override fun toString() = "Item(" +
            "alternative=$alternative, " +
            "dot=$dot)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (alternative != other.alternative) return false
        if (dot != other.dot) return false

        return true
    }

    override fun hashCode() = Objects.hash(alternative, dot)
}