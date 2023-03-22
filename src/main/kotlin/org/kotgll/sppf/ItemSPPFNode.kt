package org.kotgll.sppf

import org.kotgll.Item
import java.util.Objects

class ItemSPPFNode(
    leftExtent: Int,
    rightExtent: Int,
    val item: Item,
    ) : ParentSPPFNode(leftExtent, rightExtent) {
    override fun toString() = "ItemSPPFNode(" +
            "leftExtent=$leftExtent," +
            "rightExtent=$rightExtent, " +
            "item=$item)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemSPPFNode) return false
        if (!super.equals(other)) return false

        if (item != other.item) return false

        return true
    }

    override fun hashCode() = Objects.hash(leftExtent, rightExtent, item)
}