package org.kotgll.cfg.graphinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.cfg.graphinput.graph.GraphNode

class GLL(val startSymbol: Nonterminal, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val toPop: HashMap<Int, GSSNode> = HashMap()
  val gssNodes: HashMap<Int, GSSNode> = HashMap()
  val fakeStartSymbol: Nonterminal = Nonterminal("S'")
  val startGSSNodes: HashMap<GraphNode, Int> = makeStartGSSNodes()
  var parseSuccess: HashMap<Int, HashSet<Int>> = HashMap()

  fun makeStartGSSNodes(): HashMap<GraphNode, Int> {
    val alternative = Alternative(listOf(startSymbol))
    fakeStartSymbol.addAlternative(alternative)

    val result: HashMap<GraphNode, Int> = HashMap()
    for (node in startGraphNodes) {
      result[node] = makeGSSNode(alternative, 1, node).hashCode
    }
    return result
  }

  fun makeGSSNode(alternative: Alternative, dot: Int, ci: GraphNode): GSSNode {
    val gssNode = GSSNode(alternative, dot, ci)
    if (!gssNodes.containsKey(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
    return gssNodes[gssNode.hashCode]!!
  }

  fun parse(): HashMap<Int, HashSet<Int>> {
    for (alternative in startSymbol.alternatives) {
      for (entry in startGSSNodes.entries) {
        queue.add(alternative, 0, gssNodes[entry.value]!!, entry.key)
      }
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      if (descriptor.dot == 0) {
        parse(descriptor.alternative, descriptor.pos, descriptor.gssNode)
      } else {
        parseAt(descriptor.alternative, descriptor.dot, descriptor.pos, descriptor.gssNode)
      }
    }

    return parseSuccess
  }

  fun parse(alternative: Alternative, pos: GraphNode, cu: GSSNode) {
    if (alternative.elements.isEmpty()) {
      pop(cu, pos)
    } else {
      parseAt(alternative, 0, pos, cu)
    }
  }

  fun parseAt(alternative: Alternative, dot: Int, pos: GraphNode, cu: GSSNode) {
    if (dot < alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[dot]

      if (curSymbol is Terminal) {
        for (edge in pos.outgoingEdges) {
          val value: String? = curSymbol.match(0, edge.label)
          if (value == edge.label) {
            queue.add(alternative, dot + 1, cu, edge.head)
          }
        }
      }

      if (curSymbol is Nonterminal) {
        val curGSSNode: GSSNode = createGSSNode(alternative, dot + 1, cu, pos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, pos)
        }
      }
    } else {
      pop(cu, pos)
    }
  }

  fun pop(gssNode: GSSNode, ci: GraphNode) {
    if (gssNode.alternative.nonterminal == fakeStartSymbol && gssNode.pos.isStart && ci.isFinal) {
      if (!parseSuccess.containsKey(gssNode.pos.id)) parseSuccess[gssNode.pos.id] = HashSet()
      parseSuccess[gssNode.pos.id]!!.add(ci.id)
    }
    if (!startGSSNodes.values.contains(gssNode.hashCode)) {
      if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = gssNode
      for (u in gssNode.edges.values) {
        queue.add(gssNode.alternative, gssNode.dot, u, ci)
      }
    }
  }

  fun createGSSNode(alternative: Alternative, dot: Int, gssNode: GSSNode, ci: GraphNode): GSSNode {
    val v: GSSNode = makeGSSNode(alternative, dot, ci)

    if (v.addEdge(gssNode)) {
      if (toPop.containsKey(v.hashCode)) {
        queue.add(alternative, dot, gssNode, ci)
      }
    }

    return v
  }
}
