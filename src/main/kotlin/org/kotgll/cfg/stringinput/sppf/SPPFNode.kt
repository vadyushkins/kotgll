package org.kotgll.cfg.stringinput.sppf

import org.kotgll.cfg.grammar.symbol.Symbol
import java.util.*

open class SPPFNode(val leftExtent: Int, val rightExtent: Int) {
    override fun toString() = "SPPFNode(leftExtent=$leftExtent, rightExtent=$rightExtent)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SPPFNode) return false

        if (leftExtent != other.leftExtent) return false
        if (rightExtent != other.rightExtent) return false

        return true
    }

    open val hashCode: Int = Objects.hash(leftExtent, rightExtent)
    override fun hashCode() = hashCode

    open fun hasSymbol(symbol: Symbol) = false
}