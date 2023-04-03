package org.kotgll.rsm

import org.kotgll.rsm.grammar.RSMNonterminalEdge
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.sppf.*

class GLL(val rsmState: RSMState, val input: String) {
    val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
    val toPop: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
    val gssNodes: HashSet<GSSNode> = HashSet()
    val sppfNodes: HashSet<SPPFNode> = HashSet()

    val startState: RSMState = rsmState
    val startNonterminal: Nonterminal = rsmState.nonterminal
    val startGSSNode: GSSNode = makeGSSNode(startState, 0)

    fun makeStartGSSNode(): GSSNode {
        val fakeStartNonterminal: Nonterminal = Nonterminal("S'")
        val fakeStartRSMState: RSMState = RSMState(
            id = -2,
            nonterminal = fakeStartNonterminal,
            isStart = true,
        )
        fakeStartNonterminal.startState = fakeStartRSMState
        val fakeFinalRSMState: RSMState = RSMState(
            id = -1,
            nonterminal = fakeStartNonterminal,
            isFinal = true,
        )
        fakeStartRSMState.addNonterminalEdge(
            RSMNonterminalEdge(
                nonterminal = startNonterminal,
                head = fakeFinalRSMState,
            )
        )
        return makeGSSNode(fakeStartRSMState, 0)
    }

    fun makeGSSNode(state: RSMState, ci: Int): GSSNode {
        val gssNode = GSSNode(state, ci)
        if (!gssNodes.contains(gssNode)) {
            gssNodes.add(gssNode)
        }
        return gssNode
    }

    fun parse(): SPPFNode? {
//        if (input.isEmpty()) {
//            val cr: SPPFNode = getNodeE(0)
//            val ncn: SPPFNode = getNodeP(startState, null, cr)
//            pop(startGSSNode, ncn, 0)
//            return getResult()
//        }
//        TODO: Think
        add(startState, startGSSNode, 0, null)

        while (!queue.isEmpty()) {
            val descriptor: DescriptorsQueue.Descriptor = queue.next()
//            if (descriptor.rsmState.isStart) {
//                parse(
//                    descriptor.rsmState,
//                    descriptor.pos,
//                    descriptor.gssNode,
//                    descriptor.sppfNode,
//                )
//            }
            parseAt(
                descriptor.rsmState,
                descriptor.pos,
                descriptor.gssNode,
                descriptor.sppfNode,
            )
        }

        return getResult()
    }

    fun parse(
        state: RSMState,
        pos: Int,
        cu: GSSNode,
        cn: SPPFNode?,
    ) {
        if (state.isFinal) {
            val cr: SPPFNode = getNodeE(pos)
            val ncn: SPPFNode = getNodeP(state, cn, cr)
            pop(cu, ncn, pos)
        }
    }

    fun parseAt(
        state: RSMState,
        pos: Int,
        cu: GSSNode,
        cn: SPPFNode?,
    ) {
        var curPos: Int = pos
        var curGSSNode: GSSNode = cu
        var curSPPFNode: SPPFNode? = cn
        if (
            state.isStart && state.isFinal
        ) {
            curSPPFNode = getNodeP(state, curSPPFNode, getNodeE(pos))
        }
        for (rsmEdge in state.outgoingTerminalEdges) {
            if (curPos >= input.length) return
            val value: String? = rsmEdge.terminal.match(curPos, input)
            if (value != null) {
//                pop(curGSSNode, curSPPFNode, pos)
                val skip: Int = value.length
                val cr: SPPFNode = getNodeT(rsmEdge.terminal, value, curPos, skip)
                curSPPFNode = getNodeP(rsmEdge.head, curSPPFNode, cr)
                curPos += skip
                add(
                    rsmEdge.head, curGSSNode, curPos, curSPPFNode
                )
                return
            }
        }
        for (rsmEdge in state.outgoingNonterminalEdges) {
            curGSSNode = createGSSNode(
                rsmEdge.head,
                curGSSNode,
                curSPPFNode,
                curPos,
            )
            add(
                rsmEdge.nonterminal.startState, curGSSNode, curPos, null
            )
//            add(
//                rsmEdge.head, curGSSNode, curPos, null
//            )
        }
        pop(curGSSNode, curSPPFNode, curPos)
    }

    fun add(
        state: RSMState,
        gssNode: GSSNode,
        ci: Int,
        sppfNode: SPPFNode?,
    ) {
        queue.add(state, gssNode, ci, sppfNode)
    }

    fun getResult(): SPPFNode? {
        for (sppfNode in sppfNodes) {
            if (sppfNode.hasSymbol(startNonterminal)
                && sppfNode.leftExtent == 0
                && sppfNode.rightExtent == input.length
            ) {
                return sppfNode
            }
        }
        return null
    }

    fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, ci: Int) {
        if (gssNode != startGSSNode) {
            if (!toPop.containsKey(gssNode)) {
                toPop[gssNode] = HashSet()
            }
            toPop[gssNode]!!.add(sppfNode)
            for (e in gssNode.edges.entries) {
                for (u in e.value) {
                    add(
                        gssNode.rsmState,
                        u,
                        ci,
                        getNodeP(gssNode.rsmState, e.key, sppfNode),
                    )
                }
            }
        }
    }

    fun createGSSNode(
        state: RSMState,
        gssNode: GSSNode,
        sppfNode: SPPFNode?,
        ci: Int,
    ): GSSNode {
        val w = sppfNode
        val v: GSSNode = makeGSSNode(state, ci)

        if (v.addEdge(w, gssNode)) {
            if (toPop.containsKey(v)) {
                for (z in toPop[v]!!) {
                    add(
                        state,
                        gssNode,
                        z!!.rightExtent,
                        getNodeP(state, w, z)
                    )
                }
            }
        }

        return v
    }

    fun getNodeP(
        state: RSMState,
        sppfNode: SPPFNode?,
        nextSPPFNode: SPPFNode?,
    ): SPPFNode {
//        TODO: Think
//        if (sppfNode == null) {  // TODO: Maybe wrong
//            return nextSPPFNode
//        }

        val k = nextSPPFNode!!.leftExtent
        val i = nextSPPFNode.rightExtent
        var j = k

        if (sppfNode != null) {
            j = sppfNode.leftExtent
//            assert(sppfNode.rightExtent == k)
        }

        // TODO: Maybe wrong from here
        val y: ParentSPPFNode =
            if (state.isFinal)
                makeSymbolSPPFNode(state.nonterminal, j, i)
            else
                makeItemSPPFNode(state, j, i)
        // TODO: Maybe wrong to here

        y.kids.add(PackedSPPFNode(k, state, sppfNode, nextSPPFNode))

        return y
    }

    fun getNodeT(terminal: Terminal, value: String, i: Int, j: Int): SPPFNode {
        val y = TerminalSPPFNode(i, i + j, terminal, value)
        if (!sppfNodes.contains(y)) {
            sppfNodes.add(y)
        }
        return y
    }

    fun getNodeE(i: Int): SPPFNode {
        val y = EmptySPPFNode(i)
        if (!sppfNodes.contains(y)) {
            sppfNodes.add(y)
        }
        return y
    }

    fun makeItemSPPFNode(
        state: RSMState,
        i: Int,
        j: Int,
    ): ParentSPPFNode {
        val y = ItemSPPFNode(i, j, state)
        if (!sppfNodes.contains(y)) {
            sppfNodes.add(y)
        }
        return y
    }

    fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: Int, j: Int): ParentSPPFNode {
        val y = SymbolSPPFNode(i, j, nonterminal)
        if (!sppfNodes.contains(y)) {
            sppfNodes.add(y)
        }
        return y
    }
}