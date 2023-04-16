package org.kotgll.graph

class GraphNode(val id: Int, var isStart: Boolean = false, var isFinal: Boolean = false) {
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

  val hashCode: Int = id
  override fun hashCode() = hashCode

  fun addEdge(edge: GraphEdge) {
    outgoingEdges.add(edge)
  }
}
