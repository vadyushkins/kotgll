package org.kotgll.rsm.stringinput

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.stringinput.sppf.SPPFNode
import java.util.*

class GSSNode(val rsmState: RSMState, val k: Int) {
    val edges: MutableMap<SPPFNode?, MutableSet<GSSNode>> = HashMap()

    fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
        if (!edges.containsKey(sppfNode)) edges[sppfNode] = HashSet()
        return edges[sppfNode]!!.add(gssNode)
    }

    override fun toString() = "GSSNode(" +
            "rsmState=$rsmState, " +
            "k=$k, " +
            "edges=$edges)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GSSNode) return false

        if (rsmState != other.rsmState) return false
        if (k != other.k) return false
        if (edges != other.edges) return false

        return true
    }

    override fun hashCode() = Objects.hash(rsmState, k)
}