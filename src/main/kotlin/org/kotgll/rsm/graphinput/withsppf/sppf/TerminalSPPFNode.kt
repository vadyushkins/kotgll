package org.kotgll.rsm.graphinput.withsppf.sppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.symbol.Terminal
import java.util.*

class TerminalSPPFNode(
    leftExtent: GraphNode,
    rightExtent: GraphNode,
    val terminal: Terminal,
) : SPPFNode(leftExtent, rightExtent) {
  override fun toString() =
      "TerminalSPPFNode(leftExtent=$leftExtent, rightExtent=$rightExtent, terminal=$terminal)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is TerminalSPPFNode) return false
    if (!super.equals(other)) return false

    if (terminal != other.terminal) return false

    return true
  }

  override val hashCode: Int = Objects.hash(leftExtent, rightExtent, terminal)
  override fun hashCode() = hashCode
}
