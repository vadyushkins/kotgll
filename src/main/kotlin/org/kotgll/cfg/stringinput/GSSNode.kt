package org.kotgll.cfg.stringinput

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.stringinput.sppf.SPPFNode
import java.util.*

class GSSNode(val alternative: Alternative, val dot: Int, val k: Int) {
    val edges: HashMap<Int, HashMap<Int, GSSNode>> = HashMap()

    fun addEdge(sppfNode: SPPFNode?, gssNode: GSSNode): Boolean {
        if (!edges.containsKey(sppfNode.hashCode())) {
            edges[sppfNode.hashCode()] = HashMap()
        }
        if (!edges[sppfNode.hashCode()]!!.containsKey(gssNode.hashCode)) {
            edges[sppfNode.hashCode()]!![gssNode.hashCode] = gssNode
            return true
        }
        return false
    }

    override fun toString() = "GSSNode(alternative=$alternative, dot=$dot, k=$k, edges=$edges)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GSSNode) return false

        if (alternative != other.alternative) return false
        if (dot != other.dot) return false
        if (k != other.k) return false
        if (edges != other.edges) return false

        return true
    }

    val hashCode: Int = Objects.hash(alternative, dot, k)
    override fun hashCode() = hashCode
}