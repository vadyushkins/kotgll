package org.kotgll.cfg.stringinput.sppf

import java.util.*

open class ParentSPPFNode(leftExtent: Int, rightExtent: Int) : SPPFNode(leftExtent, rightExtent) {
    val kids: MutableSet<PackedSPPFNode> = HashSet()

    override fun toString() = "ParentSPPFNode(leftExtent=$leftExtent, rightExtent=$rightExtent)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParentSPPFNode) return false
        if (!super.equals(other)) return false

        if (kids != other.kids) return false

        return true
    }

    override val hashCode: Int = Objects.hash(leftExtent, rightExtent)
    override fun hashCode() = hashCode
}