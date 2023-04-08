package org.kotgll.vanilla.sppf

import java.util.*

class EmptySPPFNode(i: Int) : SPPFNode(i, i) {
    override fun toString() = "EmptySPPFNode(leftExtent=$leftExtent, rightExtent=$rightExtent)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmptySPPFNode) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode() = Objects.hash(leftExtent)
}