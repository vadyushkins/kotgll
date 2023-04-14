package org.kotgll.rsm.graphinput.withoutsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState

class GLL(val startState: RSMState, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val toPop: HashMap<Int, GSSNode> = HashMap()
  val gssNodes: HashMap<Int, GSSNode> = HashMap()
  val parseSuccess: HashMap<Int, HashSet<Int>> = HashMap()
  val startGSSNodes: HashMap<GraphNode, GSSNode> = makeStartGSSNodes()

  fun makeStartGSSNodes(): HashMap<GraphNode, GSSNode> {
    val result: HashMap<GraphNode, GSSNode> = HashMap()
    for (node in startGraphNodes) {
      result[node] = makeGSSNode(startState, node)
    }
    return result
  }

  fun makeGSSNode(state: RSMState, ci: GraphNode): GSSNode {
    val gssNode = GSSNode(state, ci)
    if (!gssNodes.containsKey(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
    return gssNodes[gssNode.hashCode]!!
  }

  fun parse(): HashMap<Int, HashSet<Int>> {
    for (entry in startGSSNodes.entries) {
      queue.add(startState, entry.value, entry.key)
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode)
    }

    return parseSuccess
  }

  fun parse(state: RSMState, pos: GraphNode, cu: GSSNode) {
    for (rsmEdge in state.outgoingTerminalEdges) {
      for (graphEdge in pos.outgoingEdges) {
        val value: String? = rsmEdge.terminal.match(0, graphEdge.label)
        if (value == graphEdge.label) {
          queue.add(rsmEdge.head, cu, graphEdge.head)
        }
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      val curGSSNode: GSSNode = createGSSNode(rsmEdge.head, cu, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos)
    }

    if (state.isFinal) pop(cu, pos)
  }

  fun pop(gssNode: GSSNode, ci: GraphNode) {
    if (gssNode.rsmState.id == startState.id &&
        gssNode.rsmState.nonterminal == startState.nonterminal &&
        gssNode.pos.isStart &&
        ci.isFinal) {
      if (!parseSuccess.containsKey(gssNode.pos.id)) parseSuccess[gssNode.pos.id] = HashSet()
      parseSuccess[gssNode.pos.id]!!.add(ci.id)
    }
    if (!startGSSNodes.values.contains(gssNode)) {
      if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = gssNode
      for (u in gssNode.edges.values) {
        queue.add(gssNode.rsmState, u, ci)
      }
    }
  }

  fun createGSSNode(state: RSMState, gssNode: GSSNode, ci: GraphNode): GSSNode {
    val v: GSSNode = makeGSSNode(state, ci)

    if (v.addEdge(gssNode)) {
      if (toPop.containsKey(v.hashCode)) {
        for (u in v.edges.values) {
          queue.add(state, u, ci)
        }
      }
    }

    return v
  }
}
