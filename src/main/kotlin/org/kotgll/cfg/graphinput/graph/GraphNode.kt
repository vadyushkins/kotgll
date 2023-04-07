package org.kotgll.cfg.graphinput.graph

class GraphNode(val id: Int, var isStart: Boolean = false, var isFinal: Boolean = false) {
    val hashCode: Int = id
    val outgoingEdges: MutableList<GraphEdge> = mutableListOf()

    override fun toString() = "GraphNode(id=$id, isStart=$isStart, isFinal=$isFinal)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GraphNode) return false

        if (id != other.id) return false
        if (isStart != other.isStart) return false
        if (isFinal != other.isFinal) return false

        return true
    }

    override fun hashCode() = hashCode

    fun addEdge(edge: GraphEdge) {
        outgoingEdges.add(edge)
    }
}

fun makeGraphFromString(input: String): GraphNode {
    val result = GraphNode(id = 0, isStart = true)
    var cur = result
    for (i in input.indices) {
        cur.addEdge(
            GraphEdge(
                label = input[i] + "",
                head = GraphNode(id = i + 1),
            )
        )
        cur = cur.outgoingEdges[0].head
    }
    cur.isFinal = true
    return result
}