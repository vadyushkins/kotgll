package cli

import org.junit.jupiter.api.Test
import org.kotgll.graph.GraphNode
import org.kotgll.graph.readGraphFromCSV
import org.kotgll.graph.writeGraphToCSV
import kotlin.test.assertEquals

class TestGraphReadWriteCSV {
  @Test
  fun `OneEdgeGraph CSV`() {
    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    graphNode0.addEdge("a", graphNode1)
    val expectedGraphNodes = listOf(graphNode0, graphNode1)

    val pathToCSV = "src/test/resources/cli/TestGraphReadWriteCSV/OneEdgeGraph.csv"
    writeGraphToCSV(ArrayList(expectedGraphNodes), pathToCSV)
    val actualGraphNodes = readGraphFromCSV(pathToCSV)

    assertEquals(expected = expectedGraphNodes, actual = actualGraphNodes)
  }

  @Test
  fun `TwoEdgesGraph CSV`() {
    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    graphNode0.addEdge("a", graphNode1)
    graphNode0.addEdge("b", graphNode2)
    val expectedGraphNodes = listOf(graphNode0, graphNode1, graphNode2)

    val pathToCSV = "src/test/resources/cli/TestGraphReadWriteCSV/TwoEdgesGraph.csv"
    writeGraphToCSV(ArrayList(expectedGraphNodes), pathToCSV)
    val actualGraphNodes = readGraphFromCSV(pathToCSV)

    assertEquals(expected = expectedGraphNodes, actual = actualGraphNodes)
  }

  @Test
  fun `OneCycleGraph CSV`() {
    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    graphNode0.addEdge("a", graphNode1)
    graphNode1.addEdge("a", graphNode2)
    graphNode2.addEdge("a", graphNode0)
    val expectedGraphNodes = listOf(graphNode0, graphNode1, graphNode2)

    val pathToCSV = "src/test/resources/cli/TestGraphReadWriteCSV/OneCycleGraph.csv"
    writeGraphToCSV(ArrayList(expectedGraphNodes), pathToCSV)
    val actualGraphNodes = readGraphFromCSV(pathToCSV)

    assertEquals(expected = expectedGraphNodes, actual = actualGraphNodes)
  }

  @Test
  fun `'dyck' two cycles graph CSV`() {
    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    val graphNode3 = GraphNode(id = 3, isStart = true, isFinal = true)
    val expectedGraphNodes = listOf(graphNode0, graphNode1, graphNode2, graphNode3)

    graphNode0.addEdge("(", graphNode1)
    graphNode1.addEdge("(", graphNode2)
    graphNode2.addEdge("(", graphNode0)

    graphNode2.addEdge(")", graphNode3)
    graphNode3.addEdge(")", graphNode2)

    val pathToCSV = "src/test/resources/cli/TestGraphReadWriteCSV/dyck.csv"
    writeGraphToCSV(ArrayList(expectedGraphNodes), pathToCSV)
    val actualGraphNodes = readGraphFromCSV(pathToCSV)

    assertEquals(expected = expectedGraphNodes, actual = actualGraphNodes)
  }
}
