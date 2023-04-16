package org.kotgll.rsm.graphinput.withsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.graphinput.withsppf.sppf.SPPFNode
import java.util.*

class GSSNode(val rsmState: RSMState, val k: GraphNode) {
  val edges: HashMap<SPPFNode?, HashSet<GSSNode>> = HashMap()

  fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
    if (!edges.containsKey(sppfNode)) {
      edges[sppfNode] = HashSet()
    }
    return edges[sppfNode]!!.add(gssNode)
  }

  override fun toString() = "GSSNode(rsmState=$rsmState, k=$k, edges=$edges)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (rsmState != other.rsmState) return false
    if (k != other.k) return false

    return true
  }

  val hashCode = Objects.hash(rsmState, k)
  override fun hashCode() = hashCode
}
