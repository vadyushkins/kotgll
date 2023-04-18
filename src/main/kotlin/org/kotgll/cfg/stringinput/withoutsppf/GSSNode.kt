package org.kotgll.cfg.stringinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import java.util.*

class GSSNode(
    val alternative: Alternative,
    val dot: Int,
    val pos: Int,
    val isStart: Boolean = false
) {
  val edges: HashSet<GSSNode> = HashSet()

  fun addEdge(gssNode: GSSNode) = edges.add(gssNode)

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
