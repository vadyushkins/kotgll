package org.kotgll.cfg.stringinput.sppf

import org.kotgll.cfg.grammar.Alternative
import java.util.*

open class PackedSPPFNode(
    val pivot: Int,
    val alternative: Alternative,
    val dot: Int,
    val leftSPPFNode: SPPFNode? = null,
    val rightSPPFNode: SPPFNode? = null,
) {
    override fun toString() = "PackedSPPFNode(" +
            "pivot=$pivot, " +
            "alternative=$alternative, " +
            "dot=$dot, " +
            "leftSPPFNode=$leftSPPFNode, " +
            "rightSPPFNode=$rightSPPFNode)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PackedSPPFNode) return false

        if (pivot != other.pivot) return false
        if (alternative != other.alternative) return false
        if (dot != other.dot) return false
        if (leftSPPFNode != other.leftSPPFNode) return false
        if (rightSPPFNode != other.rightSPPFNode) return false

        return true
    }

    val hashCode: Int = Objects.hash(pivot, alternative, dot, leftSPPFNode, rightSPPFNode)
    override fun hashCode() = hashCode
}