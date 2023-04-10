package org.kotgll.cfg.stringinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Symbol
import org.kotgll.cfg.grammar.symbol.Terminal

class GLL(val startSymbol: Nonterminal, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val toPop: HashMap<Int, GSSNode> = HashMap()
  val gssNodes: HashMap<Int, GSSNode> = HashMap()

  val fakeStartSymbol: Nonterminal = Nonterminal("S'")
  val startGSSNode: GSSNode = makeStartGSSNode()

  var parseSuccess: Boolean = false

  fun makeStartGSSNode(): GSSNode {
    val alternative = Alternative(listOf(startSymbol))
    fakeStartSymbol.addAlternative(alternative)
    return makeGSSNode(alternative, 1, 0)
  }

  fun makeGSSNode(alternative: Alternative, dot: Int, ci: Int): GSSNode {
    val gssNode = GSSNode(alternative, dot, ci)
    if (!gssNodes.contains(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
    return gssNode
  }

  fun parse(): Boolean {
    for (alternative in startSymbol.alternatives) {
      queue.add(alternative, 0, startGSSNode, 0)
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

  fun parse(alternative: Alternative, pos: Int, cu: GSSNode) {
    if (alternative.elements.isEmpty()) {
      pop(cu, pos)
    } else {
      parseAt(alternative, 0, pos, cu)
    }
  }

  fun parseAt(alternative: Alternative, dot: Int, pos: Int, cu: GSSNode) {
    var curDot: Int = dot
    var curPos: Int = pos
    var curGSSNode: GSSNode = cu
    while (curDot < alternative.elements.size) {
      val curSymbol: Symbol = alternative.elements[curDot]

      if (curSymbol is Terminal) {
        if (curPos >= input.length) return
        val value: String? = curSymbol.match(curPos, input)
        if (value != null) {
          val skip: Int = value.length
          curPos += skip
          curDot += 1
          continue
        }
        return
      }

      if (curSymbol is Nonterminal) {
        curGSSNode = createGSSNode(alternative, curDot + 1, curGSSNode, curPos)
        for (alt in curSymbol.alternatives) {
          queue.add(alt, 0, curGSSNode, curPos)
        }
        return
      }
    }
    pop(curGSSNode, curPos)
  }

  fun pop(gssNode: GSSNode, ci: Int) {
    if (gssNode.alternative.nonterminal == fakeStartSymbol &&
        gssNode.pos == 0 &&
        ci == input.length)
        parseSuccess = true
    if (gssNode != startGSSNode) {
      if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = gssNode
      for (u in gssNode.edges.values) {
        queue.add(gssNode.alternative, gssNode.dot, u, ci)
      }
    }
  }

  fun createGSSNode(alternative: Alternative, dot: Int, gssNode: GSSNode, ci: Int): GSSNode {
    val v: GSSNode = makeGSSNode(alternative, dot, ci)

    if (v.addEdge(gssNode)) {
      if (toPop.containsKey(v.hashCode)) {
        for (u in v.edges.values) {
          queue.add(alternative, dot, u, ci)
        }
      }
    }

    return v
  }
}
