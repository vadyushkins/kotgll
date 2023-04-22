package org.kotgll.cfg.graphinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.graph.GraphNode

class GLL(val startSymbol: Nonterminal, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val poppedGSSNodes: HashMap<GSSNode, HashSet<GraphNode>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  var parseResult: HashMap<Int, HashSet<Int>> = HashMap()

  fun getOrCreateGSSNode(nonterminal: Nonterminal, pos: GraphNode): GSSNode {
    val gssNode = GSSNode(nonterminal, pos)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashSet<Int>> {
    for (alternative in startSymbol.alternatives) {
      for (graphNode in startGraphNodes) {
        queue.add(alternative, 0, getOrCreateGSSNode(startSymbol, graphNode), graphNode)
      }
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      if (descriptor.dot == 0 && descriptor.alternative.elements.isEmpty()) {
        pop(descriptor.gssNode, descriptor.pos)
      } else {
        parse(descriptor.alternative, descriptor.dot, descriptor.gssNode, descriptor.pos)
      }
    }

    return parseResult
  }

  fun parse(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: GraphNode) {
    if (dot < alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[dot]

      if (curSymbol is Terminal && pos.outgoingEdges.containsKey(curSymbol.value)) {
        for (head in pos.outgoingEdges[curSymbol.value]!!) {
          queue.add(alternative, dot + 1, gssNode, head)
        }
      }

      if (curSymbol is Nonterminal) {
        val curGSSNode: GSSNode = createGSSNode(alternative, dot + 1, gssNode, pos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, pos)
        }
      }
    } else {
      pop(gssNode, pos)
    }
  }

  fun pop(gssNode: GSSNode, pos: GraphNode) {
    if (gssNode.nonterminal == startSymbol && gssNode.pos.isStart && pos.isFinal) {
      if (!parseResult.containsKey(gssNode.pos.id)) parseResult[gssNode.pos.id] = HashSet()
      parseResult[gssNode.pos.id]!!.add(pos.id)
    }
    if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
    poppedGSSNodes[gssNode]!!.add(pos)
    for (e in gssNode.edges) {
      for (u in e.value) {
        queue.add(e.key.first, e.key.second, u, pos)
      }
    }
  }

  fun createGSSNode(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: GraphNode): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(alternative.elements[dot - 1] as Nonterminal, pos)

    if (v.addEdge(alternative, dot, gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(alternative, dot, gssNode, z)
        }
      }
    }

    return v
  }
}
