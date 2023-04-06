package org.kotgll.cfg.graphinput.sppf

import org.kotgll.cfg.graphinput.graph.GraphNode
import java.util.*

open class ParentSPPFNode(leftExtent: GraphNode, rightExtent: GraphNode) : SPPFNode(leftExtent, rightExtent) {
    val kids: MutableSet<PackedSPPFNode> = HashSet()

    override fun toString() = "ParentSPPFNode(" +
            "leftExtent=$leftExtent, " +
            "rightExtent=$rightExtent, " +
            "kids=$kids)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParentSPPFNode) return false
        if (!super.equals(other)) return false

        if (kids != other.kids) return false

        return true
    }

    override fun hashCode() = Objects.hash(leftExtent, rightExtent, kids)
}