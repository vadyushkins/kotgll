package org.kotgll.graph

import java.io.File

fun writeGraphToCSV(graph: ArrayList<GraphNode>, pathToCSV: String) {
  val edges: ArrayList<Triple<GraphNode, String, GraphNode>> = ArrayList()

  val visited: HashSet<GraphNode> = HashSet()
  val queue: ArrayDeque<GraphNode> = ArrayDeque(graph)
  while (!queue.isEmpty()) {
    val v = queue.removeFirst()
    visited.add(v)
    for (edge in v.outgoingEdges) {
      for (head in edge.value) {
        val newEdge = Triple(v, edge.key, head)
        if (!edges.contains(newEdge)) edges.add(newEdge)
        if (!visited.contains(head)) queue.addFirst(head)
      }
    }
  }

  File(pathToCSV).printWriter().use { out ->
    edges.forEach { out.println("${it.first.id} ${it.third.id} ${it.second}") }
  }
}
