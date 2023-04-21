package org.kotgll.rsm.stringinput.withoutsppf

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal

class GLL(val startState: RSMState, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val poppedGSSNodes: HashMap<GSSNode, HashSet<Int>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  var parseResult: Boolean = false

  fun getOrCreateGSSNode(nonterminal: Nonterminal, pos: Int): GSSNode {
    val gssNode = GSSNode(nonterminal, pos)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): Boolean {
    queue.add(startState, getOrCreateGSSNode(startState.nonterminal, 0), 0)

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.gssNode, descriptor.pos)
    }

    return parseResult
  }

  fun parse(state: RSMState, gssNode: GSSNode, pos: Int) {
    for (rsmEdge in state.outgoingTerminalEdges) {
      if (pos >= input.length) break
      if (rsmEdge.terminal.match(pos, input)) {
        queue.add(rsmEdge.head, gssNode, pos + rsmEdge.terminal.size)
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      val curGSSNode = createGSSNode(rsmEdge.nonterminal, rsmEdge.head, gssNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos)
    }

    if (state.isFinal) pop(gssNode, pos)
  }

  fun pop(gssNode: GSSNode, pos: Int) {
    if (!parseResult &&
        gssNode.nonterminal == startState.nonterminal &&
        gssNode.pos == 0 &&
        pos == input.length)
        parseResult = true
    if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
    poppedGSSNodes[gssNode]!!.add(pos)
    for (e in gssNode.edges.entries) {
      for (u in e.value) {
        queue.add(e.key, u, pos)
      }
    }
  }

  fun createGSSNode(
      nonterminal: Nonterminal,
      state: RSMState,
      gssNode: GSSNode,
      pos: Int
  ): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(nonterminal, pos)

    if (v.addEdge(state, gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(state, gssNode, z)
        }
      }
    }

    return v
  }
}
