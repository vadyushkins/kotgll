package org.kotgll.cfg.stringinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal

class GLL(val startSymbol: Nonterminal, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val poppedGSSNodes: HashMap<GSSNode, HashSet<Int>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  var parseResult: Boolean = false

  fun getOrCreateGSSNode(nonterminal: Nonterminal, pos: Int): GSSNode {
    val gssNode = GSSNode(nonterminal, pos)
    if (!createdGSSNodes.contains(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): Boolean {
    for (alternative in startSymbol.alternatives) {
      queue.add(alternative, 0, getOrCreateGSSNode(startSymbol, 0), 0)
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
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, createGSSNode(alternative, curDot + 1, gssNode, curPos), curPos)
        }
        return
      }
    }
    pop(gssNode, curPos)
  }

  fun pop(gssNode: GSSNode, pos: Int) {
    if (!parseResult &&
        gssNode.nonterminal == startSymbol &&
        gssNode.pos == 0 &&
        pos == input.length)
        parseResult = true
    if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
    poppedGSSNodes[gssNode]!!.add(pos)
    for (e in gssNode.edges.entries) {
      for (u in e.value) {
        queue.add(e.key.first, e.key.second, u, pos)
      }
    }
  }

  fun createGSSNode(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: Int): GSSNode {
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
