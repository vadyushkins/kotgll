package org.kotgll.graph

import java.io.File

fun writeGraphToCSV(graph: List<GraphNode>, pathToCSV: String) {
  val edges: HashMap<GraphNode, MutableList<GraphEdge>> = HashMap()

  val visited: HashSet<GraphNode> = HashSet()
  val queue: ArrayDeque<GraphNode> = ArrayDeque(graph)
  while (!queue.isEmpty()) {
    val v = queue.removeFirst()
    visited.add(v)
    for (edge in v.outgoingEdges) {
      if (!edges.containsKey(v)) edges[v] = mutableListOf()
      if (!edges[v]!!.contains(edge)) edges[v]!!.add(edge)
      if (!visited.contains(edge.head)) queue.addFirst(edge.head)
    }
  }

  File(pathToCSV).printWriter().use { out ->
    edges.keys.forEach { tail ->
      edges[tail]!!.forEach { edge -> out.println("${tail.id} ${edge.head.id} ${edge.label}") }
    }
  }
}
