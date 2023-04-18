package org.kotgll.cfg.stringinput.withsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.cfg.stringinput.withsppf.sppf.*

class GLL(val startSymbol: Nonterminal, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val poppedGSSNodes: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val createdSPPFNodes: HashMap<SPPFNode, SPPFNode> = HashMap()
  val startGSSNode: GSSNode = makeStartGSSNode()
  var parseResult: SPPFNode? = null

  fun makeStartGSSNode(): GSSNode {
    val s1 = Nonterminal("S'")
    val alternative = Alternative(listOf(startSymbol))
    s1.addAlternative(alternative)
    return getOrCreateGSSNode(alternative, 1, 0, true)
  }

  fun getOrCreateGSSNode(
      alternative: Alternative,
      dot: Int,
      pos: Int,
      isStart: Boolean = false,
  ): GSSNode {
    val gssNode = GSSNode(alternative, dot, pos, isStart)
    if (!createdGSSNodes.contains(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): SPPFNode? {
    for (alternative in startSymbol.alternatives) {
      queue.add(alternative, 0, startGSSNode, null, 0)
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
            descriptor.pos,
        )
      }
    }

    return parseResult
  }

  fun parse(alternative: Alternative, dot: Int, gssNode: GSSNode, sppfNode: SPPFNode?, pos: Int) {
    var curPos: Int = pos
    var curGSSNode: GSSNode = gssNode
    var curSPPFNode: SPPFNode? = sppfNode
    for (i in dot until alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[i]

      if (curSymbol is Terminal) {
        if (curPos >= input.length) return
        if (curSymbol.match(curPos, input)) {
          val nextSPPFNode: SPPFNode =
              getOrCreateTerminalSPPFNode(curSymbol, curPos, curSymbol.size)
          curSPPFNode = getNodeP(alternative, i + 1, curSPPFNode, nextSPPFNode)
          curPos += curSymbol.size
          continue
        }
        return
      }

      if (curSymbol is Nonterminal) {
        curGSSNode = createGSSNode(alternative, i + 1, curGSSNode, curSPPFNode, curPos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, null, curPos)
        }
        return
      }
    }
    pop(curGSSNode, curSPPFNode, curPos)
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, pos: Int) {
    if (!gssNode.isStart) {
      if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
      poppedGSSNodes[gssNode]!!.add(sppfNode)
      for (e in gssNode.edges.entries) {
        for (u in e.value) {
          queue.add(
              gssNode.alternative,
              gssNode.dot,
              u,
              getNodeP(gssNode.alternative, gssNode.dot, createdSPPFNodes[e.key], sppfNode!!),
              pos,
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
      pos: Int,
  ): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(alternative, dot, pos)

    if (v.addEdge(sppfNode, gssNode)) {
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

    val newSPPFNode =
        if (dot == alternative.elements.size)
            getOrCreateSymbolSPPFNode(alternative.nonterminal, leftExtent, rightExtent)
        else getOrCreateItemSPPFNode(alternative, dot, leftExtent, rightExtent)

    newSPPFNode.kids.add(
        PackedSPPFNode(nextSPPFNode.leftExtent, alternative, dot, sppfNode, nextSPPFNode))

    return newSPPFNode
  }

  fun getOrCreateTerminalSPPFNode(terminal: Terminal, leftExtent: Int, rightExtent: Int): SPPFNode {
    val newSPPFNode = TerminalSPPFNode(leftExtent, leftExtent + rightExtent, terminal)
    if (!createdSPPFNodes.containsKey(newSPPFNode)) createdSPPFNodes[newSPPFNode] = newSPPFNode
    return createdSPPFNodes[newSPPFNode]!!
  }

  fun getOrCreateItemSPPFNode(
      alternative: Alternative,
      dot: Int,
      leftExtent: Int,
      rightExtent: Int
  ): ItemSPPFNode {
    val newSPPFNode = ItemSPPFNode(leftExtent, rightExtent, alternative, dot)
    if (!createdSPPFNodes.containsKey(newSPPFNode)) createdSPPFNodes[newSPPFNode] = newSPPFNode
    return createdSPPFNodes[newSPPFNode]!! as ItemSPPFNode
  }

  fun getOrCreateSymbolSPPFNode(
      nonterminal: Nonterminal,
      leftExtent: Int,
      rightExtent: Int
  ): SymbolSPPFNode {
    val newSPPFNode = SymbolSPPFNode(leftExtent, rightExtent, nonterminal)
    if (!createdSPPFNodes.containsKey(newSPPFNode)) createdSPPFNodes[newSPPFNode] = newSPPFNode
    val result = createdSPPFNodes[newSPPFNode]!! as SymbolSPPFNode
    if (parseResult == null &&
        nonterminal == startSymbol &&
        leftExtent == 0 &&
        rightExtent == input.length)
        parseResult = result
    return result
  }
}
