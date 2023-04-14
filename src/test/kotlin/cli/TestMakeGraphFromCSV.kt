package cli

import makeGraphFromCSV
import org.junit.jupiter.api.Test
import org.kotgll.graph.GraphEdge
import org.kotgll.graph.GraphNode
import java.io.File
import kotlin.test.assertEquals

class TestMakeGraphFromCSV {
  @Test
  fun `make OneEdgeGraph from CSV`() {
    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    val expectedGraphNodes = listOf(graphNode0, graphNode1)

    val pathToCSV = "src/test/resources/cli/TestMakeGraphFromCSV/OneEdgeGraph.csv"
    val actualGraphNodes = makeGraphFromCSV(File(pathToCSV).inputStream())

    assertEquals(expected = expectedGraphNodes, actual = actualGraphNodes)
  }

  @Test
  fun `make TwoEdgesGraph from CSV`() {
    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode0.addEdge(GraphEdge(label = "b", head = graphNode2))
    val expectedGraphNodes = listOf(graphNode0, graphNode1, graphNode2)

    val pathToCSV = "src/test/resources/cli/TestMakeGraphFromCSV/TwoEdgesGraph.csv"
    val actualGraphNodes = makeGraphFromCSV(File(pathToCSV).inputStream())

    assertEquals(expected = expectedGraphNodes, actual = actualGraphNodes)
  }
}
