package org.kotgll.cfg.stringinput

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.cfg.stringinput.sppf.*

class GLL(val startSymbol: Nonterminal, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val toPop: HashMap<Int, HashMap<Int, SPPFNode?>> = HashMap()
  val gssNodes: HashMap<Int, GSSNode> = HashMap()
  val sppfNodes: HashMap<Int, SPPFNode> = HashMap()

  val startGSSNode: GSSNode = makeStartGSSNode()

  fun makeStartGSSNode(): GSSNode {
    val s1 = Nonterminal("S'")
    val alternative = Alternative(listOf(startSymbol))
    s1.addAlternative(alternative)
    return makeGSSNode(alternative, 1, 0)
  }

  fun makeGSSNode(alternative: Alternative, dot: Int, ci: Int): GSSNode {
    val gssNode = GSSNode(alternative, dot, ci)
    if (!gssNodes.contains(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
    return gssNode
  }

  fun parse(): SPPFNode? {
    for (alternative in startSymbol.alternatives) {
      queue.add(alternative, 0, startGSSNode, 0, null)
    }

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      if (descriptor.dot == 0) {
        parse(
            descriptor.alternative,
            descriptor.pos,
            descriptor.gssNode,
            descriptor.sppfNode,
        )
      } else {
        parseAt(
            descriptor.alternative,
            descriptor.dot,
            descriptor.pos,
            descriptor.gssNode,
            descriptor.sppfNode,
        )
      }
    }

    for (sppfNode in sppfNodes.values) {
      if (sppfNode.hasSymbol(startSymbol) &&
          sppfNode.leftExtent == 0 &&
          sppfNode.rightExtent == input.length)
          return sppfNode
    }
    return null
  }

  fun parse(alternative: Alternative, pos: Int, cu: GSSNode, cn: SPPFNode?) {
    if (alternative.elements.isEmpty()) {
      val cr: SPPFNode = getNodeE(pos)
      val ncn: SPPFNode? = getNodeP(alternative, 0, cn, cr)
      pop(cu, ncn, pos)
    } else {
      parseAt(alternative, 0, pos, cu, cn)
    }
  }

  fun parseAt(alternative: Alternative, dot: Int, pos: Int, cu: GSSNode, cn: SPPFNode?) {
    var curPos: Int = pos
    var curGSSNode: GSSNode = cu
    var curSPPFNode: SPPFNode? = cn
    for (i in dot until alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[i]

      if (curSymbol is Terminal) {
        if (curPos >= input.length) return
        val value: String? = curSymbol.match(curPos, input)
        if (value != null) {
          val skip: Int = value.length
          val cr: SPPFNode = getNodeT(curSymbol, curPos, skip)
          curSPPFNode = getNodeP(alternative, i + 1, curSPPFNode, cr)
          curPos += skip
          continue
        }
        return
      }

      if (curSymbol is Nonterminal) {
        curGSSNode = createGSSNode(alternative, i + 1, curGSSNode, curSPPFNode, curPos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, curPos, null)
        }
        return
      }
    }
    pop(curGSSNode, curSPPFNode, curPos)
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, ci: Int) {
    if (gssNode != startGSSNode) {
      if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = HashMap()
      toPop[gssNode.hashCode]!![sppfNode.hashCode()] = sppfNode
      for (e in gssNode.edges.entries) {
        for (u in e.value.values) {
          queue.add(
              gssNode.alternative,
              gssNode.dot,
              u,
              ci,
              getNodeP(gssNode.alternative, gssNode.dot, sppfNodes[e.key], sppfNode),
          )
        }
      }
    }
  }

  fun createGSSNode(
      alternative: Alternative,
      dot: Int,
      gssNode: GSSNode,
      sppfNode: SPPFNode?,
      ci: Int
  ): GSSNode {
    val v: GSSNode = makeGSSNode(alternative, dot, ci)

    if (v.addEdge(sppfNode, gssNode)) {
      if (toPop.containsKey(v.hashCode)) {
        for (z in toPop[v.hashCode]!!.values) {
          queue.add(
              alternative, dot, gssNode, z!!.rightExtent, getNodeP(alternative, dot, sppfNode, z))
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
    if (dot == 1 && alternative.elements.size > 1) return nextSPPFNode

    val k = nextSPPFNode!!.leftExtent
    val i = nextSPPFNode.rightExtent
    var j = k

    if (sppfNode != null) j = sppfNode.leftExtent

    val y: ParentSPPFNode =
        if (dot == alternative.elements.size) makeSymbolSPPFNode(alternative.nonterminal, j, i)
        else makeItemSPPFNode(alternative, dot, j, i)

    y.kids.add(PackedSPPFNode(k, alternative, dot, sppfNode, nextSPPFNode))

    return y
  }

  fun getNodeT(terminal: Terminal, i: Int, j: Int): SPPFNode {
    val y = TerminalSPPFNode(i, i + j, terminal)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!!
  }

  fun getNodeE(i: Int): SPPFNode {
    val y = EmptySPPFNode(i)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!!
  }

  fun makeItemSPPFNode(alternative: Alternative, dot: Int, i: Int, j: Int): ParentSPPFNode {
    val y = ItemSPPFNode(i, j, alternative, dot)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!! as ParentSPPFNode
  }

  fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: Int, j: Int): ParentSPPFNode {
    val y = SymbolSPPFNode(i, j, nonterminal)
    if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
    return sppfNodes[y.hashCode]!! as ParentSPPFNode
  }
}
