package org.kotgll.cfg.stringinput.withsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.stringinput.withsppf.sppf.SPPFNode
import java.util.*

class GSSNode(val nonterminal: Nonterminal, val pos: Int) {
  val edges: HashMap<Triple<Alternative, Int, SPPFNode?>, HashSet<GSSNode>> = HashMap()

  fun addEdge(alternative: Alternative, dot: Int, sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
    val label = Triple(alternative, dot, sppfNode)
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

  val hashCode: Int = Objects.hash(nonterminal, pos)
  override fun hashCode() = hashCode
}
