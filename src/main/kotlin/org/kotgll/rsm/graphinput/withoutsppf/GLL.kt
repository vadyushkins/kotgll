package org.kotgll.rsm.graphinput.withoutsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState

class GLL(val startState: RSMState, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val poppedGSSNodes: HashMap<GSSNode, HashSet<GraphNode>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val startGSSNodes: MutableMap<GraphNode, GSSNode> = makeStartGSSNodes()
  val parseResult: HashMap<Int, HashSet<Int>> = HashMap()

  fun makeStartGSSNodes(): HashMap<GraphNode, GSSNode> {
    val result: HashMap<GraphNode, GSSNode> = HashMap()
    for (node in startGraphNodes) {
      result[node] = getOrCreateGSSNode(startState, node, true)
    }
    return result
  }

  fun getOrCreateGSSNode(state: RSMState, pos: GraphNode, isStart: Boolean = false): GSSNode {
    val gssNode = GSSNode(state, pos, isStart)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashSet<Int>> {
    for (entry in startGSSNodes.entries) {
      queue.add(startState, entry.value, entry.key)
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode)
    }

    return parseResult
  }

  fun parse(state: RSMState, pos: GraphNode, gssNode: GSSNode) {
    for (rsmEdge in state.outgoingTerminalEdges) {
      for (graphEdge in pos.outgoingEdges) {
        if (rsmEdge.terminal.value == graphEdge.label) {
          queue.add(rsmEdge.head, gssNode, graphEdge.head)
        }
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      val curGSSNode: GSSNode = createGSSNode(rsmEdge.head, gssNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos)
    }

    if (state.isFinal) pop(gssNode, pos)
  }

  fun pop(gssNode: GSSNode, pos: GraphNode) {
    if (gssNode.rsmState.id == startState.id &&
        gssNode.rsmState.nonterminal == startState.nonterminal &&
        gssNode.pos.isStart &&
        pos.isFinal) {
      if (!parseResult.containsKey(gssNode.pos.id)) parseResult[gssNode.pos.id] = HashSet()
      parseResult[gssNode.pos.id]!!.add(pos.id)
    }
    if (!gssNode.isStart) {
      if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
      poppedGSSNodes[gssNode]!!.add(pos)
      for (u in gssNode.edges) {
        queue.add(gssNode.rsmState, u, pos)
      }
    }
  }

  fun createGSSNode(state: RSMState, gssNode: GSSNode, pos: GraphNode): GSSNode {
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
