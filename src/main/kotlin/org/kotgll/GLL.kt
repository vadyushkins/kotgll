package org.kotgll

import org.kotgll.sppf.*
import org.kotgll.symbol.Nonterminal
import org.kotgll.symbol.Symbol
import org.kotgll.symbol.Terminal
import org.kotgll.sppf.TerminalSPPFNode

class GLL(
    val symbol: Nonterminal,
    val input: String,
) {
    val queue: DescriptorsQueue = DescriptorsQueue(input.length + 1)
    val toPop: HashMap<GSSNode, HashSet<SPPFNode?>> = HashMap()
    val gssNodes: HashMap<GSSNode, GSSNode> = HashMap()
    val sppfNodes: HashMap<SPPFNode, SPPFNode> = HashMap()

    val startSymbol: Nonterminal = symbol
    val startGSSNode: GSSNode = makeStartGSSNode()

    fun makeStartGSSNode(): GSSNode {
        val s1 = Nonterminal("S'")
        val alternative = Alternative(listOf<Symbol>(symbol))
        s1.addAlternative(alternative)
        return makeGSSNode(Item(alternative, 1), 0)
    }

    fun makeGSSNode(item: Item, ci: Int): GSSNode {
        val gssNode: GSSNode = GSSNode(item, ci)
        if (!gssNodes.containsKey(gssNode)) {
            gssNodes[gssNode] = gssNode
        }
        return gssNodes[gssNode]!!
    }

    fun parse(): SPPFNode? {
        for (alternative in startSymbol) {
            add(alternative, startGSSNode, 0, null)
        }

        while (!queue.isEmpty()) {
            val descriptor: DescriptorsQueue.Descriptor = queue.next()
            descriptor.parse(this)
        }

        return getResult()
    }

    fun add(parser: Parser, gssNode: GSSNode, ci: Int, sppfNode: SPPFNode?) {
        queue.add(parser, gssNode, ci, sppfNode)
    }

    fun getResult(): SPPFNode? {
        for (sppfNode in sppfNodes.keys) {
            if (sppfNode.hasSymbol(startSymbol)
                && sppfNode.leftExtent == 0
                && sppfNode.rightExtent == input.length) {
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
                    add(gssNode.item, u, ci, getNodeP(gssNode.item as Item, e.key, sppfNode))
                }
            }
        }
    }

    fun createGSSNode(item: Item, gssNode: GSSNode, sppfNode: SPPFNode?, ci: Int): GSSNode {
        val w = sppfNode
        val v: GSSNode = makeGSSNode(item, ci)

        if (v.addEdge(w, gssNode)) {
            if (toPop.containsKey(v)) {
                for (z in toPop[v]!!) {
                    add(item, gssNode, z!!.rightExtent, getNodeP(item, w, z))
                }
            }
        }

        return v
    }

    fun charAt(pos: Int) = input[pos]

    fun getNodeP(item: Item, sppfNode: SPPFNode?, nextSPPFNode: SPPFNode?): SPPFNode? {
        if (item.dot == 1 && item.alternative.elements.size > 1) {
            assert(sppfNode == null)
            return nextSPPFNode
        }

        val k = nextSPPFNode!!.leftExtent
        val i = nextSPPFNode!!.rightExtent
        var j = k

        if (sppfNode != null) {
            j = sppfNode.leftExtent
//            assert(sppfNode.rightExtent == k)
        }

        val y: ParentSPPFNode =
            if (item.isAtEnd())
                makeSymbolSPPFNode(item.alternative.nonterminal, j, i)
            else
                makeItemSPPFNode(item, j, i)

        y.kids.add(PackedSPPFNode(k, item, sppfNode, nextSPPFNode))

        return y
    }

    fun getNodeT(terminal: Terminal, value: String, i: Int, j: Int): SPPFNode {
        val y = TerminalSPPFNode(i, i + j, terminal, value)
        if (!sppfNodes.containsKey(y)) {
            sppfNodes[y] = y
        }
        return sppfNodes[y]!!
    }

    fun getNodeE(i: Int): SPPFNode {
        val y = EmptySPPFNode(i)
        if (!sppfNodes.containsKey(y)) {
            sppfNodes[y] = y
        }
        return sppfNodes[y]!!
    }

    fun makeItemSPPFNode(item: Item, i: Int, j: Int): ParentSPPFNode {
        val y = ItemSPPFNode(i, j, item)
        if (!sppfNodes.containsKey(y)) {
            sppfNodes[y] = y
        }
        return sppfNodes[y]!! as ParentSPPFNode
    }

    fun makeSymbolSPPFNode(nonterminal: Nonterminal, i: Int, j: Int): ParentSPPFNode {
        val y = SymbolSPPFNode(i, j, nonterminal)
        if (!sppfNodes.containsKey(y)) {
            sppfNodes[y] = y
        }
        return sppfNodes[y]!! as ParentSPPFNode
    }

    fun isAtEnd(pos: Int) = pos >= input.length
}