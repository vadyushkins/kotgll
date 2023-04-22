package org.kotgll.rsm.graphinput.withoutsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal

class GLL(val startState: RSMState, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val poppedGSSNodes: HashMap<GSSNode, HashSet<GraphNode>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val parseResult: HashMap<Int, HashSet<Int>> = HashMap()

  fun getOrCreateGSSNode(nonterminal: Nonterminal, pos: GraphNode): GSSNode {
    val gssNode = GSSNode(nonterminal, pos)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashSet<Int>> {
    for (graphNode in startGraphNodes) {
      queue.add(startState, getOrCreateGSSNode(startState.nonterminal, graphNode), graphNode)
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode)
    }

    return parseResult
  }

  fun parse(state: RSMState, pos: GraphNode, gssNode: GSSNode) {
    for (rsmEdge in state.outgoingTerminalEdges) {
      if (pos.outgoingEdges.containsKey(rsmEdge.terminal.value)) {
        for (head in pos.outgoingEdges[rsmEdge.terminal.value]!!) {
          queue.add(rsmEdge.head, gssNode, head)
        }
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      val curGSSNode: GSSNode = createGSSNode(rsmEdge.nonterminal, rsmEdge.head, gssNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos)
    }

    if (state.isFinal) pop(gssNode, pos)
  }

  fun pop(gssNode: GSSNode, pos: GraphNode) {
    if (gssNode.nonterminal == startState.nonterminal && gssNode.pos.isStart && pos.isFinal) {
      if (!parseResult.containsKey(gssNode.pos.id)) parseResult[gssNode.pos.id] = HashSet()
      parseResult[gssNode.pos.id]!!.add(pos.id)
    }
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
      pos: GraphNode
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
