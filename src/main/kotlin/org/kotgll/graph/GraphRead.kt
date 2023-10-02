package org.kotgll.graph

import java.io.File

fun readGraphFromString(input: String): GraphNode {
  val result = GraphNode(id = 0, isStart = true)
  var cur = result
  for (i in input.indices) {
    val head = GraphNode(id = i + 1)
    cur.addEdge(input[i] + "", head)
    cur = head
  }
  cur.isFinal = true
  return result
}

fun readGraphFromCSV(pathToCSV: String): ArrayList<GraphNode> {
  val graphNodes: HashMap<Int, GraphNode> = HashMap()
  fun makeGraphNode(id: Int, isStart: Boolean = false, isFinal: Boolean = false): GraphNode {
    val y = GraphNode(id, isStart, isFinal)
    if (!graphNodes.containsKey(y.id)) graphNodes[y.id] = y
    return graphNodes[y.id]!!
  }

  val reader = File(pathToCSV).inputStream().bufferedReader()
  while (true) {
    val line: String = reader.readLine() ?: break
    val (tail, head, label) = line.split(' ', limit = 3)

    val tailGraphNode = makeGraphNode(id = tail.toInt(), isStart = true, isFinal = true)
    val headGraphNode = makeGraphNode(id = head.toInt(), isStart = true, isFinal = true)

    tailGraphNode.addEdge(label, headGraphNode)
  }

  return ArrayList(graphNodes.values)
}

fun readStartNodesFromCSV(pathToCSV: String): ArrayList<GraphNode> {
  val graphNodes: HashMap<Int, GraphNode> = HashMap()
  fun makeGraphNode(id: Int, isStart: Boolean = false, isFinal: Boolean = false): GraphNode {
    val y = GraphNode(id, isStart, isFinal)
    if (!graphNodes.containsKey(y.id)) graphNodes[y.id] = y
    return graphNodes[y.id]!!
  }

  val reader = File(pathToCSV).inputStream().bufferedReader()
  while (true) {
    val node: Int = reader.readLine()?.toInt() ?: break
    makeGraphNode(node, isStart = true, isFinal = true)
  }

  return ArrayList(graphNodes.values)
}
