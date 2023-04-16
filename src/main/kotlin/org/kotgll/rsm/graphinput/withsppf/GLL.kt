package org.kotgll.rsm.graphinput.withsppf

import org.kotgll.graph.GraphNode
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.graphinput.withsppf.sppf.*

class GLL(val startState: RSMState, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val toPop: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
  val gssNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val sppfNodes: HashMap<SPPFNode, SPPFNode> = HashMap()
  val parseResult: HashMap<Int, HashMap<Int, SPPFNode>> = HashMap()
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
    if (!gssNodes.containsKey(gssNode)) gssNodes[gssNode] = gssNode
    return gssNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashMap<Int, SPPFNode>>? {
    for (entry in startGSSNodes.entries) {
      queue.add(startState, entry.value, entry.key, null)
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode, descriptor.sppfNode)
    }

    for (sppfNode in sppfNodes.values) {
      if (sppfNode.hasSymbol(startState.nonterminal) &&
          sppfNode.leftExtent.isStart &&
          sppfNode.rightExtent.isFinal) {
        if (!parseResult.containsKey(sppfNode.leftExtent.id)) {
          parseResult[sppfNode.leftExtent.id] = HashMap()
        }
        parseResult[sppfNode.leftExtent.id]!![sppfNode.rightExtent.id] = sppfNode
      }
    }
    if (HashMap<Int, HashMap<Int, SPPFNode>>() == parseResult) return null
    return parseResult
  }

  fun parse(state: RSMState, pos: GraphNode, cu: GSSNode, cn: SPPFNode?) {
    var curSPPFNode: SPPFNode? = cn

    if (state.isStart && state.isFinal) curSPPFNode = getNodeP(state, curSPPFNode, getNodeE(pos))

    for (rsmEdge in state.outgoingTerminalEdges) {
      for (graphEdge in pos.outgoingEdges) {
        val value: String? = rsmEdge.terminal.match(0, graphEdge.label)
        if (value == graphEdge.label) {
          val cr: SPPFNode = getNodeT(rsmEdge.terminal, pos, graphEdge.head)
          queue.add(rsmEdge.head, cu, graphEdge.head, getNodeP(rsmEdge.head, curSPPFNode, cr))
        }
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      val curGSSNode: GSSNode = createGSSNode(rsmEdge.head, cu, curSPPFNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos, null)
    }

    if (state.isFinal) pop(cu, curSPPFNode, pos)
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, ci: GraphNode) {
    if (!startGSSNodes.values.contains(gssNode)) {
      if (!toPop.containsKey(gssNode)) toPop[gssNode] = HashSet()
      toPop[gssNode]!!.add(sppfNode)
      for (e in gssNode.edges.entries) {
        for (u in e.value) {
          queue.add(
              gssNode.rsmState, u, ci, getNodeP(gssNode.rsmState, sppfNodes[e.key], sppfNode!!))
        }
      }
    }
  }

  fun createGSSNode(
      state: RSMState,
      gssNode: GSSNode,
      sppfNode: SPPFNode?,
      ci: GraphNode
  ): GSSNode {
    val v: GSSNode = makeGSSNode(state, ci)

    if (v.addEdge(sppfNode, gssNode)) {
      if (toPop.containsKey(v)) {
        for (z in toPop[v]!!) {
          queue.add(state, gssNode, z!!.rightExtent, getNodeP(state, sppfNode, z))
        }
      }
    }

    return v
  }

  fun getNodeP(state: RSMState, sppfNode: SPPFNode?, nextSPPFNode: SPPFNode): SPPFNode {
    val k = nextSPPFNode.leftExtent
    val i = nextSPPFNode.rightExtent
    var j = k

    if (sppfNode != null) j = sppfNode.leftExtent

    val y: ParentSPPFNode =
        if (state.isFinal) makeSymbolSPPFNode(state.nonterminal, j, i)
        else makeItemSPPFNode(state, j, i)

    y.kids.add(PackedSPPFNode(k, state, sppfNode, nextSPPFNode))

    return y
  }

  fun getNodeT(terminal: Terminal, i: GraphNode, j: GraphNode): SPPFNode {
    val y = TerminalSPPFNode(i, j, terminal)
    if (!sppfNodes.containsKey(y)) sppfNodes[y] = y
    return sppfNodes[y]!!
  }

  fun getNodeE(i: GraphNode): SPPFNode {
    val y = EmptySPPFNode(i)
    if (!sppfNodes.containsKey(y)) sppfNodes[y] = y
    return sppfNodes[y]!!
  }

  fun makeItemSPPFNode(state: RSMState, i: GraphNode, j: GraphNode): ParentSPPFNode {
    val y = ItemSPPFNode(i, j, state)
    if (!sppfNodes.containsKey(y)) sppfNodes[y] = y
    return sppfNodes[y]!! as ParentSPPFNode
  }

  fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: GraphNode, j: GraphNode): ParentSPPFNode {
    val y = SymbolSPPFNode(i, j, nonterminal)
    if (!sppfNodes.containsKey(y)) sppfNodes[y] = y
    return sppfNodes[y]!! as ParentSPPFNode
  }
}
