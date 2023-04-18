package org.kotgll.rsm.stringinput.withsppf

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.stringinput.withsppf.sppf.SPPFNode
import java.util.*

class GSSNode(val rsmState: RSMState, val pos: Int, val isStart: Boolean = false) {
  val edges: MutableMap<SPPFNode?, MutableSet<GSSNode>> = HashMap()

  fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
    if (!edges.containsKey(sppfNode)) edges[sppfNode] = HashSet()
    return edges[sppfNode]!!.add(gssNode)
  }

  override fun toString() = "GSSNode(rsmState=$rsmState, pos=$pos, isStart=$isStart)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (rsmState != other.rsmState) return false
    if (pos != other.pos) return false

    return true
  }

  val hashCode: Int = Objects.hash(rsmState, pos)
  override fun hashCode() = hashCode
}
