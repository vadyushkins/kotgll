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
  val fakeStartSymbol: Nonterminal = Nonterminal("S'")
  val startGSSNodes: HashMap<GraphNode, GSSNode> = makeStartGSSNodes()
  var parseResult: HashMap<Int, HashSet<Int>> = HashMap()

  fun makeStartGSSNodes(): HashMap<GraphNode, GSSNode> {
    val alternative = Alternative(listOf(startSymbol))
    fakeStartSymbol.addAlternative(alternative)

    val result: HashMap<GraphNode, GSSNode> = HashMap()
    for (node in startGraphNodes) {
      result[node] = getOrCreateGSSNode(alternative, 1, node, true)
    }
    return result
  }

  fun getOrCreateGSSNode(
      alternative: Alternative,
      dot: Int,
      ci: GraphNode,
      isStart: Boolean = false,
  ): GSSNode {
    val gssNode = GSSNode(alternative, dot, ci, isStart)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): HashMap<Int, HashSet<Int>> {
    for (alternative in startSymbol.alternatives) {
      for (entry in startGSSNodes.entries) {
        queue.add(alternative, 0, entry.value, entry.key)
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

      if (curSymbol is Terminal) {
        for (edge in pos.outgoingEdges) {
          if (curSymbol.value == edge.label) {
            queue.add(alternative, dot + 1, gssNode, edge.head)
          }
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
    if (gssNode.alternative.nonterminal == fakeStartSymbol && gssNode.pos.isStart && pos.isFinal) {
      if (!parseResult.containsKey(gssNode.pos.id)) parseResult[gssNode.pos.id] = HashSet()
      parseResult[gssNode.pos.id]!!.add(pos.id)
    }
    if (!gssNode.isStart) {
      if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
      poppedGSSNodes[gssNode]!!.add(pos)
      for (u in gssNode.edges) {
        queue.add(gssNode.alternative, gssNode.dot, u, pos)
      }
    }
  }

  fun createGSSNode(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: GraphNode): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(alternative, dot, pos)

    if (v.addEdge(gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(alternative, dot, gssNode, z)
        }
      }
    }

    return v
  }
}
