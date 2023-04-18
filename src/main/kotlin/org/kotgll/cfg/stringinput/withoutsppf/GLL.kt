package org.kotgll.cfg.stringinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal

class GLL(val startSymbol: Nonterminal, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val poppedGSSNodes: HashMap<GSSNode, HashSet<Int>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val fakeStartSymbol: Nonterminal = Nonterminal("S'")
  val startGSSNode: GSSNode = makeStartGSSNode()
  var parseResult: Boolean = false

  fun makeStartGSSNode(): GSSNode {
    val alternative = Alternative(listOf(startSymbol))
    fakeStartSymbol.addAlternative(alternative)
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
    return gssNode
  }

  fun parse(): Boolean {
    for (alternative in startSymbol.alternatives) {
      queue.add(alternative, 0, startGSSNode, 0)
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

  fun parse(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: Int) {
    var curDot: Int = dot
    var curPos: Int = pos
    while (curDot < alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[curDot]

      if (curSymbol is Terminal) {
        if (curPos >= input.length) return
        if (curSymbol.match(curPos, input)) {
          curPos += curSymbol.size
          curDot += 1
          continue
        }
        return
      }

      if (curSymbol is Nonterminal) {
        val curGSSNode = createGSSNode(alternative, curDot + 1, gssNode, curPos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, curPos)
        }
        return
      }
    }
    pop(gssNode, curPos)
  }

  fun pop(gssNode: GSSNode, pos: Int) {
    if (!parseResult &&
        gssNode.alternative.nonterminal == fakeStartSymbol &&
        gssNode.pos == 0 &&
        pos == input.length)
        parseResult = true
    if (!gssNode.isStart) {
      if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
      poppedGSSNodes[gssNode]!!.add(pos)
      for (u in gssNode.edges) {
        queue.add(gssNode.alternative, gssNode.dot, u, pos)
      }
    }
  }

  fun createGSSNode(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: Int): GSSNode {
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
