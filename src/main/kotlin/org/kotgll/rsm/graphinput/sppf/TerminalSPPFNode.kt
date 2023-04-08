package org.kotgll.rsm.graphinput.sppf

import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.graphinput.graph.GraphNode
import java.util.*

class TerminalSPPFNode(
    leftExtent: GraphNode,
    rightExtent: GraphNode,
    val terminal: Terminal,
) : SPPFNode(leftExtent, rightExtent) {
    override val hashCode: Int = Objects.hash(leftExtent, rightExtent, terminal)
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

    override fun hashCode() = hashCode
}