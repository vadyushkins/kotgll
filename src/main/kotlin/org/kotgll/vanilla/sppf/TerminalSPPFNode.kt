package org.kotgll.vanilla.sppf

import org.kotgll.vanilla.grammar.symbol.Terminal
import java.util.*

class TerminalSPPFNode(leftExtent: Int, rightExtent: Int, val terminal: Terminal) : SPPFNode(leftExtent, rightExtent) {
    override fun toString() = "TerminalSPPFNode(" +
            "leftExtent=$leftExtent, " +
            "rightExtent=$rightExtent, " +
            "terminal=$terminal)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TerminalSPPFNode) return false
        if (!super.equals(other)) return false

        if (terminal != other.terminal) return false

        return true
    }

    override fun hashCode() = Objects.hash(leftExtent, rightExtent, terminal)
}