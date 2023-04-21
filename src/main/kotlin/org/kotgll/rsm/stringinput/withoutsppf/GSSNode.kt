package org.kotgll.rsm.stringinput.withoutsppf

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import java.util.*

class GSSNode(val nonterminal: Nonterminal, val pos: Int) {
  val edges: HashMap<RSMState, HashSet<GSSNode>> = HashMap()

  fun addEdge(rsmState: RSMState, gssNode: GSSNode): Boolean {
    if (!edges.containsKey(rsmState)) edges[rsmState] = HashSet()
    return edges[rsmState]!!.add(gssNode)
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
