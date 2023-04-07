package org.kotgll.cfg.graphinput.sppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.graphinput.graph.GraphNode
import java.util.*

class ItemSPPFNode(
    leftExtent: GraphNode,
    rightExtent: GraphNode,
    val alternative: Alternative,
    val dot: Int,
) : ParentSPPFNode(leftExtent, rightExtent) {
    override val hashCode: Int = Objects.hash(leftExtent, rightExtent, alternative, dot)
    override fun toString() = "ItemSPPFNode(" +
            "leftExtent=$leftExtent, " +
            "rightExtent=$rightExtent, " +
            "alternative=$alternative, " +
            "dot=$dot)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemSPPFNode) return false
        if (!super.equals(other)) return false

        if (alternative != other.alternative) return false
        if (dot != other.dot) return false

        return true
    }

    override fun hashCode() = hashCode
}