package org.kotgll.cfg.graphinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.graph.GraphNode
import java.util.*

class GSSNode(val nonterminal: Nonterminal, val pos: GraphNode) {
  val edges: HashMap<Pair<Alternative, Int>, HashSet<GSSNode>> = HashMap()

  fun addEdge(alternative: Alternative, dot: Int, gssNode: GSSNode): Boolean {
    val label = Pair(alternative, dot)
    if (!edges.containsKey(label)) edges[label] = HashSet()
    return edges[label]!!.add(gssNode)
  }

  override fun toString() = "GSSNode(nonterminal=$nonterminal, pos=$pos)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GSSNode) return false

    if (nonterminal != other.nonterminal) return false
    if (pos != other.pos) return false

    return true
  }

  val hashCode = Objects.hash(nonterminal, pos)
  override fun hashCode() = hashCode
}
