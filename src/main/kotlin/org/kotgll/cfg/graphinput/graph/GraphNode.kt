package org.kotgll.cfg.graphinput.graph

import java.io.InputStream

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

fun makeGraphFromString(input: String): GraphNode {
  val result = GraphNode(id = 0, isStart = true)
  var cur = result
  for (i in input.indices) {
    cur.addEdge(
        GraphEdge(
            label = input[i] + "",
            head = GraphNode(id = i + 1),
        ))
    cur = cur.outgoingEdges[0].head
  }
  cur.isFinal = true
  return result
}

fun makeGraphFromCSV(inputStream: InputStream): List<GraphNode> {
  val graphNodes: HashMap<Int, GraphNode> = HashMap()
  fun makeGraphNode(id: Int, isStart: Boolean = false, isFinal: Boolean = false): GraphNode {
    val y = GraphNode(id, isStart, isFinal)
    if (!graphNodes.containsKey(y.id)) {
      graphNodes[y.id] = y
    }
    return graphNodes[y.id]!!
  }

  val reader = inputStream.bufferedReader()
  while (true) {
    val line: String = reader.readLine() ?: break
    val (tail, head, label) = line.split(' ', ignoreCase = true, limit = 3)

    val tailGraphNode = makeGraphNode(id = tail.toInt(), isStart = true, isFinal = true)
    val headGraphNode = makeGraphNode(id = head.toInt(), isStart = true, isFinal = true)

    tailGraphNode.addEdge(GraphEdge(label = label, head = headGraphNode))
  }

  return graphNodes.values.toList()
}
