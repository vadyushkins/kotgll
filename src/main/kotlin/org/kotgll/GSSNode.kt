package org.kotgll

import org.kotgll.sppf.SPPFNode
import java.util.Objects

class GSSNode(
    val item: Parser,
    val k: Int,
) {
    val edges: MutableMap<SPPFNode?, MutableSet<GSSNode>> = HashMap()

    fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
        if (!edges.containsKey(sppfNode)) {
            edges[sppfNode] = HashSet()
        }
        return edges[sppfNode]!!.add(gssNode)
    }

    override fun toString() = "GSSNode(item=$item, k=$k, edges=$edges)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GSSNode) return false

        if (item != other.item) return false
        if (k != other.k) return false
        if (edges != other.edges) return false

        return true
    }

    override fun hashCode() = Objects.hash(item, k)
}