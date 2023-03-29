package org.kotgll.vanilla.sppf

import java.util.*

open class ParentSPPFNode(
    leftExtent: Int,
    rightExtent: Int,
) : SPPFNode(leftExtent, rightExtent) {
    val kids: MutableSet<PackedSPPFNode> = HashSet()

    override fun toString() = "ParentSPPFNode(" +
            "leftExtent=$leftExtent," +
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