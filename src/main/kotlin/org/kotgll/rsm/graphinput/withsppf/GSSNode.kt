package org.kotgll.rsm.graphinput.withsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.graphinput.withsppf.sppf.SPPFNode
import java.util.*

class GSSNode(val rsmState: RSMState, val k: GraphNode) {
  val edges: HashMap<Int, HashMap<Int, GSSNode>> = HashMap()

  fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
    if (!edges.containsKey(sppfNode.hashCode())) {
      edges[sppfNode.hashCode()] = HashMap()
    }
    if (!edges[sppfNode.hashCode()]!!.containsKey(gssNode.hashCode)) {
      edges[sppfNode.hashCode()]!![gssNode.hashCode] = gssNode
      return true
    }
    return false
  }

  override fun toString() = "GSSNode(rsmState=$rsmState, k=$k, edges=$edges)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (rsmState != other.rsmState) return false
    if (k != other.k) return false
    if (edges != other.edges) return false

    return true
  }

  val hashCode = Objects.hash(rsmState, k)
  override fun hashCode() = hashCode
}
