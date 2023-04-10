package org.kotgll.cfg.stringinput.withsppf.sppf

import java.util.*

class EmptySPPFNode(i: Int) : SPPFNode(i, i) {
  override fun toString() = "EmptySPPFNode(leftExtent=$leftExtent, rightExtent=$rightExtent)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is EmptySPPFNode) return false
    if (!super.equals(other)) return false
    return true
  }

  override val hashCode: Int = Objects.hash(leftExtent, rightExtent)
  override fun hashCode() = hashCode
}
