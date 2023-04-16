package org.kotgll.cfg.graphinput.withsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.graphinput.withsppf.sppf.SPPFNode
import org.kotgll.graph.GraphNode
import java.util.*

class GSSNode(val alternative: Alternative, val dot: Int, val k: GraphNode) {
  val edges: HashMap<SPPFNode?, HashSet<GSSNode>> = HashMap()

  fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
    if (!edges.containsKey(sppfNode)) {
      edges[sppfNode] = HashSet()
    }
    return edges[sppfNode]!!.add(gssNode)
  }

  override fun toString() = "GSSNode(alternative=$alternative, dot=$dot, k=$k)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (alternative != other.alternative) return false
    if (dot != other.dot) return false
    if (k != other.k) return false

    return true
  }

  val hashCode = Objects.hash(alternative, dot, k)
  override fun hashCode() = hashCode
}
