package org.kotgll.rsm.graphinput.withsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.graphinput.withsppf.sppf.*

class GLL(val startState: RSMState, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val poppedGSSNodes: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val createdSPPFNodes: HashMap<SPPFNode, SPPFNode> = HashMap()
  val startGSSNodes: HashMap<GraphNode, GSSNode> = makeStartGSSNodes()
  val parseResult: HashMap<Int, HashMap<Int, SPPFNode>> = HashMap()

  fun makeStartGSSNodes(): HashMap<GraphNode, GSSNode> {
    val result: HashMap<GraphNode, GSSNode> = HashMap()
    for (node in startGraphNodes) {
      result[node] = getOrCreateGSSNode(startState, node, true)
    }
    return result
  }

  fun getOrCreateGSSNode(state: RSMState, ci: GraphNode, isStart: Boolean = false): GSSNode {
    val gssNode = GSSNode(state, ci, isStart)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashMap<Int, SPPFNode>> {
    for (entry in startGSSNodes.entries) {
      queue.add(startState, entry.value, null, entry.key)
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.gssNode, descriptor.sppfNode, descriptor.pos)
    }

    return parseResult
  }

  fun parse(state: RSMState, gssNode: GSSNode, sppfNode: SPPFNode?, pos: GraphNode) {
    var curSPPFNode: SPPFNode? = sppfNode

    if (state.isStart && state.isFinal)
        curSPPFNode = getNodeP(state, curSPPFNode, getOrCreateItemSPPFNode(state, pos, pos))

    for (rsmEdge in state.outgoingTerminalEdges) {
      for (graphEdge in pos.outgoingEdges) {
        if (rsmEdge.terminal.value == graphEdge.label) {
          val nextSPPFNode: SPPFNode =
              getOrCreateTerminalSPPFNode(rsmEdge.terminal, pos, graphEdge.head)
          queue.add(
              rsmEdge.head,
              gssNode,
              getNodeP(rsmEdge.head, curSPPFNode, nextSPPFNode),
              graphEdge.head)
        }
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      val curGSSNode: GSSNode = createGSSNode(rsmEdge.head, gssNode, curSPPFNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, null, pos)
    }

    if (state.isFinal) pop(gssNode, curSPPFNode, pos)
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, pos: GraphNode) {
    if (!gssNode.isStart) {
      if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
      poppedGSSNodes[gssNode]!!.add(sppfNode)
      for (e in gssNode.edges.entries) {
        for (u in e.value) {
          queue.add(
              gssNode.rsmState,
              u,
              getNodeP(gssNode.rsmState, createdSPPFNodes[e.key], sppfNode!!),
              pos)
        }
      }
    }
  }

  fun createGSSNode(
      state: RSMState,
      gssNode: GSSNode,
      sppfNode: SPPFNode?,
      pos: GraphNode,
  ): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(state, pos)

    if (v.addEdge(sppfNode, gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(state, gssNode, getNodeP(state, sppfNode, z!!), z.rightExtent)
        }
      }
    }

    return v
  }

  fun getNodeP(state: RSMState, sppfNode: SPPFNode?, nextSPPFNode: SPPFNode): SPPFNode {
    val leftExtent = sppfNode?.leftExtent ?: nextSPPFNode.leftExtent
    val rightExtent = nextSPPFNode.rightExtent

    val y: ParentSPPFNode =
        if (state.isFinal) getOrCreateSymbolSPPFNode(state.nonterminal, leftExtent, rightExtent)
        else getOrCreateItemSPPFNode(state, leftExtent, rightExtent)

    y.kids.add(PackedSPPFNode(nextSPPFNode.leftExtent, state, sppfNode, nextSPPFNode))

    return y
  }

  fun getOrCreateTerminalSPPFNode(
      terminal: Terminal,
      leftExtent: GraphNode,
      rightExtent: GraphNode
  ): SPPFNode {
    val y = TerminalSPPFNode(leftExtent, rightExtent, terminal)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    return createdSPPFNodes[y]!!
  }

  fun getOrCreateItemSPPFNode(
      state: RSMState,
      leftExtent: GraphNode,
      rightExtent: GraphNode
  ): ParentSPPFNode {
    val y = ItemSPPFNode(leftExtent, rightExtent, state)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    return createdSPPFNodes[y]!! as ParentSPPFNode
  }

  fun getOrCreateSymbolSPPFNode(
      nonterminal: Nonterminal,
      leftExtent: GraphNode,
      rightExtent: GraphNode
  ): SymbolSPPFNode {
    val y = SymbolSPPFNode(leftExtent, rightExtent, nonterminal)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    val result = createdSPPFNodes[y]!! as SymbolSPPFNode
    if (nonterminal == startState.nonterminal && leftExtent.isStart && rightExtent.isFinal) {
      if (!parseResult.containsKey(leftExtent.id)) parseResult[leftExtent.id] = HashMap()
      parseResult[leftExtent.id]!![rightExtent.id] = result
    }
    return result
  }
}
