package org.kotgll.rsm.stringinput

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.stringinput.sppf.*

class GLL(val startState: RSMState, val input: String) {
    val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
    val toPop: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
    val gssNodes: HashSet<GSSNode> = HashSet()
    val sppfNodes: HashSet<SPPFNode> = HashSet()

    val startGSSNode: GSSNode = makeGSSNode(startState, 0)

    fun makeGSSNode(state: RSMState, ci: Int): GSSNode {
        val gssNode = GSSNode(state, ci)
        if (!gssNodes.contains(gssNode)) gssNodes.add(gssNode)
        return gssNode
    }

    fun parse(): SPPFNode? {
        queue.add(startState, startGSSNode, 0, null)

        while (!queue.isEmpty()) {
            val descriptor: DescriptorsQueue.Descriptor = queue.next()
            parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode, descriptor.sppfNode)
        }

        for (sppfNode in sppfNodes) {
            if (sppfNode.hasSymbol(startState.nonterminal)
                && sppfNode.leftExtent == 0
                && sppfNode.rightExtent == input.length
            ) return sppfNode
        }
        return null
    }

    fun parse(state: RSMState, pos: Int, cu: GSSNode, cn: SPPFNode?) {
        var curGSSNode: GSSNode
        var curSPPFNode: SPPFNode? = cn

        if (state.isStart && state.isFinal) curSPPFNode = getNodeP(state, curSPPFNode, getNodeE(pos))

        for (rsmEdge in state.outgoingTerminalEdges) {
            if (pos >= input.length) break
            val value: String? = rsmEdge.terminal.match(pos, input)
            if (value != null) {
                val skip: Int = value.length
                val cr: SPPFNode = getNodeT(rsmEdge.terminal, pos, skip)
                queue.add(rsmEdge.head, cu, pos + skip, getNodeP(rsmEdge.head, curSPPFNode, cr))
            }
        }

        for (rsmEdge in state.outgoingNonterminalEdges) {
            curGSSNode = createGSSNode(rsmEdge.head, cu, curSPPFNode, pos)
            queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos, null)
        }

        if (state.isFinal) pop(cu, curSPPFNode, pos)
    }

    fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, ci: Int) {
        if (gssNode != startGSSNode) {
            if (!toPop.containsKey(gssNode)) toPop[gssNode] = HashSet()
            toPop[gssNode]!!.add(sppfNode)
            for (e in gssNode.edges.entries) {
                for (u in e.value) {
                    val tmpSPPFNode: SPPFNode? = getNodeP(gssNode.rsmState, e.key, sppfNode)
                    if (tmpSPPFNode != null) queue.add(gssNode.rsmState, u, ci, tmpSPPFNode)
                }
            }
        }
    }

    fun createGSSNode(state: RSMState, gssNode: GSSNode, sppfNode: SPPFNode?, ci: Int): GSSNode {
        val w = sppfNode
        val v: GSSNode = makeGSSNode(state, ci)

        if (v.addEdge(w, gssNode)) {
            if (toPop.containsKey(v)) {
                for (z in toPop[v]!!) {
                    queue.add(state, gssNode, z!!.rightExtent, getNodeP(state, w, z))
                }
            }
        }

        return v
    }

    fun getNodeP(state: RSMState, sppfNode: SPPFNode?, nextSPPFNode: SPPFNode?): SPPFNode? {
        if (nextSPPFNode == null) return null

        val k = nextSPPFNode.leftExtent
        val i = nextSPPFNode.rightExtent
        var j = k

        if (sppfNode != null) j = sppfNode.leftExtent

        val y: ParentSPPFNode =
            if (state.isFinal)
                makeSymbolSPPFNode(state.nonterminal, j, i)
            else
                makeItemSPPFNode(state, j, i)

        y.kids.add(PackedSPPFNode(k, state, sppfNode, nextSPPFNode))

        return y
    }

    fun getNodeT(terminal: Terminal, i: Int, j: Int): SPPFNode {
        val y = TerminalSPPFNode(i, i + j, terminal)
        sppfNodes.add(y)
        return y
    }

    fun getNodeE(i: Int): SPPFNode {
        val y = EmptySPPFNode(i)
        sppfNodes.add(y)
        return y
    }

    fun makeItemSPPFNode(state: RSMState, i: Int, j: Int): ParentSPPFNode {
        val y = ItemSPPFNode(i, j, state)
        sppfNodes.add(y)
        return y
    }

    fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: Int, j: Int): ParentSPPFNode {
        val y = SymbolSPPFNode(i, j, nonterminal)
        sppfNodes.add(y)
        return y
    }
}