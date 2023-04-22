package org.kotgll.cfg.graphinput.withsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.cfg.graphinput.withsppf.sppf.*
import org.kotgll.graph.GraphNode

class GLL(val startSymbol: Nonterminal, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val poppedGSSNodes: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val createdSPPFNodes: HashMap<SPPFNode, SPPFNode> = HashMap()
  val parseResult: HashMap<Int, HashMap<Int, SPPFNode>> = HashMap()

  fun getOrCreateGSSNode(nonterminal: Nonterminal, pos: GraphNode): GSSNode {
    val gssNode = GSSNode(nonterminal, pos)
    if (!createdGSSNodes.contains(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashMap<Int, SPPFNode>> {
    for (alternative in startSymbol.alternatives) {
      for (graphNode in startGraphNodes) {
        queue.add(alternative, 0, getOrCreateGSSNode(startSymbol, graphNode), null, graphNode)
      }
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      if (descriptor.dot == 0 && descriptor.alternative.elements.isEmpty()) {
        pop(
            descriptor.gssNode,
            getNodeP(
                descriptor.alternative,
                0,
                descriptor.sppfNode,
                getOrCreateItemSPPFNode(descriptor.alternative, 0, descriptor.pos, descriptor.pos)),
            descriptor.pos)
      } else {
        parse(
            descriptor.alternative,
            descriptor.dot,
            descriptor.gssNode,
            descriptor.sppfNode,
            descriptor.pos)
      }
    }

    return parseResult
  }

  fun parse(
      alternative: Alternative,
      dot: Int,
      gssNode: GSSNode,
      sppfNode: SPPFNode?,
      pos: GraphNode
  ) {
    if (dot < alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[dot]

      if (curSymbol is Terminal && pos.outgoingEdges.containsKey(curSymbol.value)) {
        for (head in pos.outgoingEdges[curSymbol.value]!!) {
          queue.add(
              alternative,
              dot + 1,
              gssNode,
              getNodeP(
                  alternative,
                  dot + 1,
                  sppfNode,
                  getOrCreateTerminalSPPFNode(curSymbol, pos, head)),
              head)
        }
      }

      if (curSymbol is Nonterminal) {
        val curGSSNode: GSSNode = createGSSNode(alternative, dot + 1, gssNode, sppfNode, pos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, null, pos)
        }
      }
    } else {
      pop(gssNode, sppfNode, pos)
    }
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, pos: GraphNode) {
    if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
    poppedGSSNodes[gssNode]!!.add(sppfNode)
    for (e in gssNode.edges.entries) {
      for (u in e.value) {
        queue.add(
            e.key.first,
            e.key.second,
            u,
            getNodeP(e.key.first, e.key.second, e.key.third, sppfNode!!),
            pos,
        )
      }
    }
  }

  fun createGSSNode(
      alternative: Alternative,
      dot: Int,
      gssNode: GSSNode,
      sppfNode: SPPFNode?,
      pos: GraphNode,
  ): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(alternative.elements[dot - 1] as Nonterminal, pos)

    if (v.addEdge(alternative, dot, sppfNode, gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(
              alternative, dot, gssNode, getNodeP(alternative, dot, sppfNode, z!!), z.rightExtent)
        }
      }
    }

    return v
  }

  fun getNodeP(
      alternative: Alternative,
      dot: Int,
      sppfNode: SPPFNode?,
      nextSPPFNode: SPPFNode,
  ): SPPFNode {
    val leftExtent = sppfNode?.leftExtent ?: nextSPPFNode.leftExtent
    val rightExtent = nextSPPFNode.rightExtent

    val y =
        if (dot == alternative.elements.size)
            getOrCreateSymbolSPPFNode(alternative.nonterminal, leftExtent, rightExtent)
        else getOrCreateItemSPPFNode(alternative, dot, leftExtent, rightExtent)

    y.kids.add(PackedSPPFNode(nextSPPFNode.leftExtent, alternative, dot, sppfNode, nextSPPFNode))

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
      alternative: Alternative,
      dot: Int,
      leftExtent: GraphNode,
      rightExtent: GraphNode,
  ): ItemSPPFNode {
    val y = ItemSPPFNode(leftExtent, rightExtent, alternative, dot)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    return createdSPPFNodes[y]!! as ItemSPPFNode
  }

  fun getOrCreateSymbolSPPFNode(
      nonterminal: Nonterminal,
      leftExtent: GraphNode,
      rightExtent: GraphNode,
  ): SymbolSPPFNode {
    val y = SymbolSPPFNode(leftExtent, rightExtent, nonterminal)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    val result = createdSPPFNodes[y]!! as SymbolSPPFNode
    if (nonterminal == startSymbol && leftExtent.isStart && rightExtent.isFinal) {
      if (!parseResult.containsKey(leftExtent.id)) parseResult[leftExtent.id] = HashMap()
      parseResult[leftExtent.id]!![rightExtent.id] = result
    }
    return result
  }
}
