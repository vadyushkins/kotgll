package org.kotgll.rsm.graphinput.withoutsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState
import java.util.*

class GSSNode(val rsmState: RSMState, val pos: GraphNode, val isStart: Boolean = false) {
  val edges: HashSet<GSSNode> = HashSet()

  fun addEdge(gssNode: GSSNode) = edges.add(gssNode)

  override fun toString() = "GSSNode(rsmState=$rsmState, pos=$pos, isStart=$isStart)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (rsmState != other.rsmState) return false
    if (pos != other.pos) return false

    return true
  }

  val hashCode = Objects.hash(rsmState, pos)
  override fun hashCode() = hashCode
}
