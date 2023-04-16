package org.kotgll.cfg.stringinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import java.util.*

class GSSNode(val alternative: Alternative, val dot: Int, val pos: Int) {
  val edges: HashMap<Int, GSSNode> = HashMap()

  fun addEdge(gssNode: GSSNode): Boolean {
    if (!edges.containsKey(gssNode.hashCode)) {
      edges[gssNode.hashCode] = gssNode
      return true
    }
    return false
  }

  override fun toString() = "GSSNode(alternative=$alternative, dot=$dot, pos=$pos)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (alternative != other.alternative) return false
    if (dot != other.dot) return false
    if (pos != other.pos) return false
    if (edges != other.edges) return false

    return true
  }

  val hashCode: Int = Objects.hash(alternative, dot, pos)
  override fun hashCode() = hashCode
}
