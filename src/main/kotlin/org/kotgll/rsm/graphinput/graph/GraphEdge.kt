package org.kotgll.rsm.graphinput.graph

class GraphEdge(val label: String, val head: GraphNode) {
    override fun toString() = "GraphEdge(label=$label, head=$head)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GraphEdge) return false

        if (label != other.label) return false
        if (head != other.head) return false

        return true
    }

    val hashCode: Int = label.hashCode()
    override fun hashCode() = hashCode
}