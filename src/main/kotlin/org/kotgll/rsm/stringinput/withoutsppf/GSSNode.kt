package org.kotgll.rsm.stringinput.withoutsppf

import org.kotgll.rsm.grammar.RSMState
import java.util.*

class GSSNode(val rsmState: RSMState, val pos: Int) {
  val edges: HashMap<Int, GSSNode> = HashMap()

  fun addEdge(gssNode: GSSNode): Boolean {
    if (!edges.containsKey(gssNode.hashCode)) {
      edges[gssNode.hashCode] = gssNode
      return true
    }
    return false
  }

  override fun toString() = "GSSNode(rsmState=$rsmState, pos=$pos)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (rsmState != other.rsmState) return false
    if (pos != other.pos) return false
    if (edges != other.edges) return false

    return true
  }

  val hashCode: Int = Objects.hash(rsmState, pos)
  override fun hashCode() = hashCode
}
