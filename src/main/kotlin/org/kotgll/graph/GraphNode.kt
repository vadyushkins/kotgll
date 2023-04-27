package org.kotgll.graph

class GraphNode(val id: Int, var isStart: Boolean = false, var isFinal: Boolean = false) {
  var outgoingEdges: HashMap<String, ArrayList<GraphNode>> = HashMap()

  override fun toString() = "GraphNode(id=$id, isStart=$isStart, isFinal=$isFinal)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GraphNode) return false

    if (id != other.id) return false

    return true
  }

  val hashCode: Int = id
  override fun hashCode() = id

  fun addEdge(label: String, head: GraphNode) {
    if (!outgoingEdges.containsKey(label)) outgoingEdges[label] = ArrayList()
    if (!outgoingEdges[label]!!.contains(head)) outgoingEdges[label]!!.add(head)
  }
}
