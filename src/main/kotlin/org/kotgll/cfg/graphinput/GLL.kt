package org.kotgll.cfg.graphinput

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.cfg.graphinput.graph.GraphNode
import org.kotgll.cfg.graphinput.sppf.*

class GLL(val startSymbol: Nonterminal, val startGraphNodes: List<GraphNode>) {
  val queue: DescriptorsQueue = DescriptorsQueue()
  val toPop: HashMap<Int, HashMap<Int, SPPFNode?>> = HashMap()
  val gssNodes: HashMap<Int, GSSNode> = HashMap()
  val sppfNodes: HashMap<Int, SPPFNode> = HashMap()

  val startGSSNodes: HashMap<GraphNode, GSSNode> = makeStartGSSNodes()

  fun makeStartGSSNodes(): HashMap<GraphNode, GSSNode> {
    val s1 = Nonterminal("S'")
    val alternative = Alternative(listOf(startSymbol))
    s1.addAlternative(alternative)

    val result: HashMap<GraphNode, GSSNode> = HashMap()
    for (node in startGraphNodes) {
      result[node] = makeGSSNode(alternative, 1, node)
    }
    return result
  }

  fun makeGSSNode(alternative: Alternative, dot: Int, ci: GraphNode): GSSNode {
    val gssNode = GSSNode(alternative, dot, ci)
    if (!gssNodes.containsKey(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
    return gssNodes[gssNode.hashCode]!!
  }

  fun parse(): List<SPPFNode>? {
    for (alternative in startSymbol.alternatives) {
      for (entry in startGSSNodes.entries) {
        queue.add(alternative, 0, entry.value, entry.key, null)
      }
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      if (descriptor.dot == 0) {
        parse(descriptor.alternative, descriptor.pos, descriptor.gssNode, descriptor.sppfNode)
      } else {
        parseAt(
          descriptor.alternative,
          descriptor.dot,
          descriptor.pos,
          descriptor.gssNode,
          descriptor.sppfNode
        )
      }
    }

    val result: MutableList<SPPFNode> = mutableListOf()
    for (sppfNode in sppfNodes.values) {
      if (
        sppfNode.hasSymbol(startSymbol) &&
          sppfNode.leftExtent.isStart &&
          sppfNode.rightExtent.isFinal
      )
        result.add(sppfNode)
    }
    if (result.isEmpty()) return null
    return result.toList()
  }

  fun parse(alternative: Alternative, pos: GraphNode, cu: GSSNode, cn: SPPFNode?) {
    if (alternative.elements.isEmpty()) {
      val cr: SPPFNode = getNodeE(pos)
      val ncn: SPPFNode? = getNodeP(alternative, 0, cn, cr)
      pop(cu, ncn, pos)
    } else {
      parseAt(alternative, 0, pos, cu, cn)
    }
  }

  fun parseAt(alternative: Alternative, dot: Int, pos: GraphNode, cu: GSSNode, cn: SPPFNode?) {
    if (dot < alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[dot]

      if (curSymbol is Terminal) {
        for (edge in pos.outgoingEdges) {
          val value: String? = curSymbol.match(0, edge.label)
          if (value == edge.label) {
            val cr: SPPFNode = getNodeT(curSymbol, pos, edge.head)
            queue.add(alternative, dot + 1, cu, edge.head, getNodeP(alternative, dot + 1, cn, cr))
          }
        }
      }

      if (curSymbol is Nonterminal) {
        val curGSSNode: GSSNode = createGSSNode(alternative, dot + 1, cu, cn, pos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, pos, null)
        }
      }
    } else {
      pop(cu, cn, pos)
    }
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, ci: GraphNode) {
    if (!startGSSNodes.values.contains(gssNode)) {
      if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = HashMap()
      toPop[gssNode.hashCode]!![sppfNode.hashCode()] = sppfNode
      for (e in gssNode.edges.entries) {
        for (u in e.value.values) {
          val tmpSPPFNode = getNodeP(gssNode.alternative, gssNode.dot, sppfNodes[e.key], sppfNode)
          if (tmpSPPFNode != null) queue.add(gssNode.alternative, gssNode.dot, u, ci, tmpSPPFNode)
        }
      }
    }
  }

  fun createGSSNode(
    alternative: Alternative,
    dot: Int,
    gssNode: GSSNode,
    sppfNode: SPPFNode?,
    ci: GraphNode,
  ): GSSNode {
    val v: GSSNode = makeGSSNode(alternative, dot, ci)

    if (v.addEdge(sppfNode, gssNode)) {
      if (toPop.containsKey(v.hashCode)) {
        for (z in toPop[v.hashCode]!!.values) {
          queue.add(
            alternative,
            dot,
            gssNode,
            z!!.rightExtent,
            getNodeP(alternative, dot, sppfNode, z)
          )
        }
      }
    }

    return v
  }

  fun getNodeP(
    alternative: Alternative,
    dot: Int,
    sppfNode: SPPFNode?,
    nextSPPFNode: SPPFNode?
  ): SPPFNode? {
    if (nextSPPFNode == null) return null

    val k = nextSPPFNode.leftExtent
    val i = nextSPPFNode.rightExtent
    var j = k

    if (sppfNode != null) j = sppfNode.leftExtent

    val y: ParentSPPFNode =
      if (dot == alternative.elements.size) makeSymbolSPPFNode(alternative.nonterminal, j, i)
      else makeItemSPPFNode(alternative, dot, j, i)

    y.kids.add(PackedSPPFNode(k, alternative, dot, sppfNode, nextSPPFNode))

    return y
  }

  fun getNodeT(terminal: Terminal, i: GraphNode, j: GraphNode): SPPFNode {
    val y = TerminalSPPFNode(i, j, terminal)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!!
  }

  fun getNodeE(i: GraphNode): SPPFNode {
    val y = EmptySPPFNode(i)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!!
  }

  fun makeItemSPPFNode(
    alternative: Alternative,
    dot: Int,
    i: GraphNode,
    j: GraphNode
  ): ParentSPPFNode {
    val y = ItemSPPFNode(i, j, alternative, dot)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!! as ParentSPPFNode
  }

  fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: GraphNode, j: GraphNode): ParentSPPFNode {
    val y = SymbolSPPFNode(i, j, nonterminal)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!! as ParentSPPFNode
  }
}
