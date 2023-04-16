package org.kotgll.rsm.stringinput.withoutsppf

import org.kotgll.rsm.grammar.RSMState

class GLL(val startState: RSMState, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val toPop: HashMap<Int, GSSNode> = HashMap()
  val gssNodes: HashMap<Int, GSSNode> = HashMap()

  val startGSSNode: GSSNode = makeGSSNode(startState, 0)

  var parseSuccess: Boolean = false

  fun makeGSSNode(state: RSMState, ci: Int): GSSNode {
    val gssNode = GSSNode(state, ci)
    if (!gssNodes.containsKey(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
    return gssNodes[gssNode.hashCode]!!
  }

  fun parse(): Boolean {
    queue.add(startState, startGSSNode, 0)

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode)
    }

    return parseSuccess
  }

  fun parse(state: RSMState, pos: Int, cu: GSSNode) {
    var curGSSNode: GSSNode

    for (rsmEdge in state.outgoingTerminalEdges) {
      if (pos >= input.length) break
      val value: String? = rsmEdge.terminal.match(pos, input)
      if (value != null) {
        val skip: Int = value.length
        queue.add(rsmEdge.head, cu, pos + skip)
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      curGSSNode = createGSSNode(rsmEdge.head, cu, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos)
    }

    if (state.isFinal) pop(cu, pos)
  }

  fun pop(gssNode: GSSNode, ci: Int) {
    if (gssNode.rsmState.id == startState.id &&
        gssNode.rsmState.nonterminal == startState.nonterminal &&
        gssNode.pos == 0 &&
        ci == input.length)
        parseSuccess = true
    if (gssNode != startGSSNode) {
      if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = gssNode
      for (u in gssNode.edges.values) {
        queue.add(gssNode.rsmState, u, ci)
      }
    }
  }

  fun createGSSNode(state: RSMState, gssNode: GSSNode, ci: Int): GSSNode {
    val v: GSSNode = makeGSSNode(state, ci)

    if (v.addEdge(gssNode)) {
      if (toPop.containsKey(v.hashCode)) {
        for (u in v.edges.values) {
          queue.add(state, u, ci)
        }
      }
    }

    return v
  }
}
