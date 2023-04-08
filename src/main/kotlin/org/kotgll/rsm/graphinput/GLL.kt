package org.kotgll.rsm.graphinput

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.graphinput.graph.GraphNode
import org.kotgll.rsm.graphinput.sppf.*

class GLL(val startState: RSMState, val startGraphNodes: List<GraphNode>) {
    val queue: DescriptorsQueue = DescriptorsQueue()
    val toPop: HashMap<Int, HashMap<Int, SPPFNode?>> = HashMap()
    val gssNodes: HashMap<Int, GSSNode> = HashMap()
    val sppfNodes: HashMap<Int, SPPFNode> = HashMap()

    val startGSSNodes: HashMap<GraphNode, GSSNode> = makeStartGSSNodes()

    fun makeStartGSSNodes(): HashMap<GraphNode, GSSNode> {
        val result: HashMap<GraphNode, GSSNode> = HashMap()
        for (node in startGraphNodes) {
            result[node] = makeGSSNode(startState, node)
        }
        return result
    }

    fun makeGSSNode(state: RSMState, ci: GraphNode): GSSNode {
        val gssNode = GSSNode(state, ci)
        if (!gssNodes.containsKey(gssNode.hashCode)) gssNodes[gssNode.hashCode] = gssNode
        return gssNodes[gssNode.hashCode]!!
    }

    fun parse(): List<SPPFNode>? {
        for (entry in startGSSNodes.entries) {
            queue.add(startState, entry.value, entry.key, null)
        }

        while (!queue.isEmpty()) {
            val descriptor: DescriptorsQueue.Descriptor = queue.next()
            parse(descriptor.rsmState, descriptor.pos, descriptor.gssNode, descriptor.sppfNode)
        }

        val result: MutableList<SPPFNode> = mutableListOf()
        for (sppfNode in sppfNodes.values) {
            if (sppfNode.hasSymbol(startState.nonterminal)
                && sppfNode.leftExtent.isStart
                && sppfNode.rightExtent.isFinal
            ) result.add(sppfNode)
        }
        if (result.isEmpty()) return null
        return result.toList()
    }

    fun parse(state: RSMState, pos: GraphNode, cu: GSSNode, cn: SPPFNode?) {
        var curSPPFNode: SPPFNode? = cn

        if (state.isStart && state.isFinal) curSPPFNode = getNodeP(state, curSPPFNode, getNodeE(pos))

        for (rsmEdge in state.outgoingTerminalEdges) {
            for (graphEdge in pos.outgoingEdges) {
                val value: String? = rsmEdge.terminal.match(0, graphEdge.label)
                if (value == graphEdge.label) {
                    val cr: SPPFNode = getNodeT(rsmEdge.terminal, pos, graphEdge.head)
                    queue.add(rsmEdge.head, cu, graphEdge.head, getNodeP(rsmEdge.head, curSPPFNode, cr))
                }
            }
        }

        for (rsmEdge in state.outgoingNonterminalEdges) {
            val curGSSNode: GSSNode = createGSSNode(rsmEdge.head, cu, curSPPFNode, pos)
            queue.add(rsmEdge.nonterminal.startState, curGSSNode, pos, null)
        }

        if (state.isFinal) pop(cu, curSPPFNode, pos)
    }

    fun pop(gssNode: GSSNode, sppfNode: SPPFNode?, ci: GraphNode) {
        if (!startGSSNodes.values.contains(gssNode)) {
            if (!toPop.containsKey(gssNode.hashCode)) toPop[gssNode.hashCode] = HashMap()
            toPop[gssNode.hashCode]!![sppfNode.hashCode()] = sppfNode
            for (e in gssNode.edges.entries) {
                for (u in e.value.values) {
                    val tmpSPPFNode = getNodeP(gssNode.rsmState, sppfNodes[e.key], sppfNode)
                    if (tmpSPPFNode != null) queue.add(gssNode.rsmState, u, ci, tmpSPPFNode)
                }
            }
        }
    }

    fun createGSSNode(state: RSMState, gssNode: GSSNode, sppfNode: SPPFNode?, ci: GraphNode): GSSNode {
        val v: GSSNode = makeGSSNode(state, ci)

        if (v.addEdge(sppfNode, gssNode)) {
            if (toPop.containsKey(v.hashCode)) {
                for (z in toPop[v.hashCode]!!.values) {
                    queue.add(state, gssNode, z!!.rightExtent, getNodeP(state, sppfNode, z))
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

    fun makeItemSPPFNode(state: RSMState, i: GraphNode, j: GraphNode): ParentSPPFNode {
        val y = ItemSPPFNode(i, j, state)
        if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
        return sppfNodes[y.hashCode]!! as ParentSPPFNode
    }

    fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: GraphNode, j: GraphNode): ParentSPPFNode {
        val y = SymbolSPPFNode(i, j, nonterminal)
        if (!sppfNodes.containsKey(y.hashCode)) sppfNodes[y.hashCode] = y
        return sppfNodes[y.hashCode]!! as ParentSPPFNode
    }
}