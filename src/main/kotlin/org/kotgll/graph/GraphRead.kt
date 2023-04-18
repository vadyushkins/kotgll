import org.kotgll.graph.GraphEdge
import org.kotgll.graph.GraphNode
import java.io.File

fun readGraphFromString(input: String): GraphNode {
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

fun readGraphFromCSV(pathToCSV: String): List<GraphNode> {
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

    tailGraphNode.addEdge(GraphEdge(label = label, head = headGraphNode))
  }

  return graphNodes.values.toList()
}
