package org.kotgll.rsm.stringinput.withoutsppf

import org.kotgll.rsm.grammar.RSMState

class GLL(val startState: RSMState, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val poppedGSSNodes: HashMap<GSSNode, HashSet<Int>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val startGSSNode: GSSNode = getOrCreateGSSNode(startState, 0, true)
  var parseResult: Boolean = false

  fun getOrCreateGSSNode(state: RSMState, ci: Int, isStart: Boolean = false): GSSNode {
    val gssNode = GSSNode(state, ci, isStart)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): Boolean {
    queue.add(startState, startGSSNode, 0)

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
      val curGSSNode = createGSSNode(rsmEdge.head, gssNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos)
    }

    if (state.isFinal) pop(gssNode, pos)
  }

  fun pop(gssNode: GSSNode, pos: Int) {
    if (!parseResult &&
        gssNode.rsmState.id == startState.id &&
        gssNode.rsmState.nonterminal == startState.nonterminal &&
        gssNode.pos == 0 &&
        pos == input.length)
        parseResult = true
    if (!gssNode.isStart) {
      if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
      poppedGSSNodes[gssNode]!!.add(pos)
      for (u in gssNode.edges) {
        queue.add(gssNode.rsmState, u, pos)
      }
    }
  }

  fun createGSSNode(state: RSMState, gssNode: GSSNode, pos: Int): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(state, pos)

    if (v.addEdge(gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(state, gssNode, z)
        }
      }
    }

    return v
  }
}
