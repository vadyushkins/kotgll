package org.kotgll.vanilla

import org.kotgll.vanilla.grammar.Alternative
import org.kotgll.vanilla.sppf.SPPFNode
import java.util.*

class GSSNode(
    val alternative: Alternative,
    val dot: Int,
    val k: Int,
) {
    val edges: MutableMap<SPPFNode?, MutableSet<GSSNode>> = HashMap()

    fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
        if (!edges.containsKey(sppfNode)) {
            edges[sppfNode] = HashSet()
        }
        return edges[sppfNode]!!.add(gssNode)
    }

    override fun toString() = "GSSNode(" +
            "alternative=$alternative, " +
            "dot=$dot, " +
            "k=$k, " +
            "edges=$edges)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GSSNode) return false

        if (alternative != other.alternative) return false
        if (dot != other.dot) return false
        if (k != other.k) return false
        if (edges != other.edges) return false

        return true
    }

    override fun hashCode() = Objects.hash(alternative, dot, k)
}