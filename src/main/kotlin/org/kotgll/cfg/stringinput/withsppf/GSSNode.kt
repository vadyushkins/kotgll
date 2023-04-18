package org.kotgll.cfg.stringinput.withsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.stringinput.withsppf.sppf.SPPFNode
import java.util.*

class GSSNode(
    val alternative: Alternative,
    val dot: Int,
    val pos: Int,
    var isStart: Boolean = false,
) {
  val edges: HashMap<SPPFNode?, HashSet<GSSNode>> = HashMap()

  fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
    if (!edges.containsKey(sppfNode)) edges[sppfNode] = HashSet()
    return edges[sppfNode]!!.add(gssNode)
  }

  override fun toString() =
      "GSSNode(alternative=$alternative, dot=$dot, pos=$pos, isStart=$isStart)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (alternative != other.alternative) return false
    if (dot != other.dot) return false
    if (pos != other.pos) return false

    return true
  }

  val hashCode: Int = Objects.hash(alternative, dot, pos)
  override fun hashCode() = hashCode
}
