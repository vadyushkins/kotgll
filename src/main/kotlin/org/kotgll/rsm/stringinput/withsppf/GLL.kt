package org.kotgll.rsm.stringinput.withsppf

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.stringinput.withsppf.sppf.*

class GLL(val startState: RSMState, val input: String) {
  val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
  val poppedGSSNodes: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
  val createdGSSNodes: HashMap<GSSNode, GSSNode> = HashMap()
  val createdSPPFNodes: HashMap<SPPFNode, SPPFNode> = HashMap()
  var parseResult: SPPFNode? = null

  fun getOrCreateGSSNode(nonterminal: Nonterminal, pos: Int): GSSNode {
    val gssNode = GSSNode(nonterminal, pos)
    if (!createdGSSNodes.containsKey(gssNode)) createdGSSNodes[gssNode] = gssNode
    return createdGSSNodes[gssNode]!!
  }

  fun parse(): SPPFNode? {
    queue.add(startState, getOrCreateGSSNode(startState.nonterminal, 0), null, 0)

    while (!queue.isEmpty()) {
      val descriptor: DescriptorsQueue.Descriptor = queue.next()
      parse(descriptor.rsmState, descriptor.gssNode, descriptor.sppfNode, descriptor.pos)
    }

    return parseResult
  }

  fun parse(state: RSMState, gssNode: GSSNode, sppfNode: SPPFNode?, pos: Int) {
    var curGSSNode: GSSNode
    var curSPPFNode: SPPFNode? = sppfNode

    if (state.isStart && state.isFinal)
        curSPPFNode = getNodeP(state, curSPPFNode, getOrCreateItemSPPFNode(state, pos, pos))

    for (rsmEdge in state.outgoingTerminalEdges) {
      if (pos >= input.length) break
      if (rsmEdge.terminal.match(pos, input)) {
        val nextSPPFNode: SPPFNode =
            getOrCreateTerminalSPPFNode(rsmEdge.terminal, pos, rsmEdge.terminal.size)
        queue.add(
            rsmEdge.head,
            gssNode,
            getNodeP(rsmEdge.head, curSPPFNode, nextSPPFNode),
            pos + rsmEdge.terminal.size)
      }
    }

    for (rsmEdge in state.outgoingNonterminalEdges) {
      curGSSNode = createGSSNode(rsmEdge.nonterminal, rsmEdge.head, gssNode, curSPPFNode, pos)
      queue.add(rsmEdge.nonterminal.startState, curGSSNode, null, pos)
    }

    if (state.isFinal) pop(gssNode, curSPPFNode, pos)
  }

  fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, pos: Int) {
    if (!poppedGSSNodes.containsKey(gssNode)) poppedGSSNodes[gssNode] = HashSet()
    poppedGSSNodes[gssNode]!!.add(sppfNode)
    for (e in gssNode.edges.entries) {
      for (u in e.value) {
        queue.add(e.key.first, u, getNodeP(e.key.first, e.key.second, sppfNode!!), pos)
      }
    }
  }

  fun createGSSNode(
      nonterminal: Nonterminal,
      state: RSMState,
      gssNode: GSSNode,
      sppfNode: SPPFNode?,
      pos: Int
  ): GSSNode {
    val v: GSSNode = getOrCreateGSSNode(nonterminal, pos)

    if (v.addEdge(state, sppfNode, gssNode)) {
      if (poppedGSSNodes.containsKey(v)) {
        for (z in poppedGSSNodes[v]!!) {
          queue.add(state, gssNode, getNodeP(state, sppfNode, z!!), z.rightExtent)
        }
      }
    }

    return v
  }

  fun getNodeP(state: RSMState, sppfNode: SPPFNode?, nextSPPFNode: SPPFNode): SPPFNode {
    val leftExtent = sppfNode?.leftExtent ?: nextSPPFNode.leftExtent
    val rightExtent = nextSPPFNode.rightExtent

    val y =
        if (state.isFinal) getOrCreateSymbolSPPFNode(state.nonterminal, leftExtent, rightExtent)
        else getOrCreateItemSPPFNode(state, leftExtent, rightExtent)

    y.kids.add(PackedSPPFNode(nextSPPFNode.leftExtent, state, sppfNode, nextSPPFNode))

    return y
  }

  fun getOrCreateTerminalSPPFNode(terminal: Terminal, leftExtent: Int, rightExtent: Int): SPPFNode {
    val y = TerminalSPPFNode(leftExtent, leftExtent + rightExtent, terminal)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    return createdSPPFNodes[y]!!
  }

  fun getOrCreateItemSPPFNode(state: RSMState, leftExtent: Int, rightExtent: Int): ItemSPPFNode {
    val y = ItemSPPFNode(leftExtent, rightExtent, state)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    return createdSPPFNodes[y]!! as ItemSPPFNode
  }

  fun getOrCreateSymbolSPPFNode(
      nonterminal: Nonterminal,
      leftExtent: Int,
      rightExtent: Int
  ): SymbolSPPFNode {
    val y = SymbolSPPFNode(leftExtent, rightExtent, nonterminal)
    if (!createdSPPFNodes.containsKey(y)) createdSPPFNodes[y] = y
    val result = createdSPPFNodes[y]!! as SymbolSPPFNode
    if (parseResult == null &&
        nonterminal == startState.nonterminal &&
        leftExtent == 0 &&
        rightExtent == input.length)
        parseResult = result
    return result
  }
}
