package org.kotgll.sppf

import org.kotgll.Item
import java.util.Objects

open class PackedSPPFNode(
    val pivot: Int,
    val item: Item,
    val leftSPPFNode: SPPFNode? = null,
    val rightSPPFNode: SPPFNode? = null,
) {
    override fun toString() = "PackedSPPFNode(" +
            "pivot=$pivot, " +
            "item=$item, " +
            "leftSPPFNode=$leftSPPFNode, " +
            "rightSPPFNode=$rightSPPFNode)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PackedSPPFNode) return false

        if (pivot != other.pivot) return false
        if (item != other.item) return false
        if (leftSPPFNode != other.leftSPPFNode) return false
        if (rightSPPFNode != other.rightSPPFNode) return false

        return true
    }

    override fun hashCode() = Objects.hash(pivot, item, leftSPPFNode, rightSPPFNode)
}