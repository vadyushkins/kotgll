package org.kotgll.rsm.stringinput.sppf

import org.kotgll.rsm.grammar.RSMState
import java.util.*

open class PackedSPPFNode(
    val pivot: Int,
    val rsmState: RSMState,
    val leftSPPFNode: SPPFNode? = null,
    val rightSPPFNode: SPPFNode? = null
) {
  override fun toString() =
      "PackedSPPFNode(pivot=$pivot, rsmState=$rsmState, leftSPPFNode=$leftSPPFNode, rightSPPFNode=$rightSPPFNode)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PackedSPPFNode) return false

    if (pivot != other.pivot) return false
    if (rsmState != other.rsmState) return false
    if (leftSPPFNode != other.leftSPPFNode) return false
    if (rightSPPFNode != other.rightSPPFNode) return false

    return true
  }

  val hashCode: Int = Objects.hash(pivot, rsmState, leftSPPFNode, rightSPPFNode)
  override fun hashCode() = hashCode
}
