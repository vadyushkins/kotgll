package cfg.graphinput.withoutsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.graphinput.withoutsppf.GLL
import org.kotgll.graph.GraphEdge
import org.kotgll.graph.GraphNode
import org.kotgll.graph.readGraphFromString
import kotlin.test.assertEquals

class TestCFGGraphInputWithoutSPPFSuccess {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf()))

    val graph = GraphNode(id = 0, isStart = true, isFinal = true)

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(0)),
        actual = GLL(nonterminalS, listOf(graph)).parse(),
    )
  }

  @Test
  fun `test 'a' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(1)),
        actual = GLL(nonterminalS, listOf(readGraphFromString("a"))).parse(),
    )
  }

  @Test
  fun `test 'ab' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(2)),
        actual = GLL(nonterminalS, listOf(readGraphFromString("ab"))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["", "a", "aa", "aaa", "aaaa"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("a")))))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(input.length)),
        actual = GLL(nonterminalS, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["a", "aa", "aaa"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Plus(Terminal("a")))))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(input.length)),
        actual = GLL(nonterminalS, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["", "ab", "abab", "ababab"])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("ab")))))

    val graph = GraphNode(id = 0, isStart = true)
    var cur = graph
    var i = 0
    while (i < input.length) {
      cur.addEdge(GraphEdge(label = "" + input[i] + input[i + 1], head = GraphNode(id = i + 1)))
      cur = cur.outgoingEdges[0].head
      i += 2
    }
    cur.isFinal = true

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(cur.id)),
        actual = GLL(nonterminalS, listOf(graph)).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(
      strings =
          [
              "",
              "()",
              "()()",
              "()()()",
              "(())",
              "(())()",
              "(())()()",
              "(())(())",
              "(())(())()",
              "(())(())()()",
              "(()())(()())",
              "((()))",
              "(((())))",
              "((((()))))",
          ])
  fun `test 'dyck' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf()))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("("), nonterminalS, Terminal(")"), nonterminalS)))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(input.length)),
        actual = GLL(nonterminalS, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("cd"))))

    val graph = GraphNode(id = 0, isStart = true)
    graph.addEdge(GraphEdge(label = input, head = GraphNode(id = 1, isFinal = true)))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(1)),
        actual = GLL(nonterminalS, listOf(graph)).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["", "a"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Optional(Terminal("a")))))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(input.length)),
        actual = GLL(nonterminalS, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["abc"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    nonterminalS.addAlternative(Alternative(listOf(nonterminalA, Terminal("c"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalB, Terminal("c"))))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("b"))))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(input.length)),
        actual = GLL(nonterminalS, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be NotEmpty for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")
    nonterminalS.addAlternative(Alternative(listOf(nonterminalA)))
    nonterminalS.addAlternative(Alternative(listOf(nonterminalB)))

    nonterminalA.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("cd"))))

    nonterminalB.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("cd"))))

    val graph = GraphNode(id = 0, isStart = true)
    graph.addEdge(GraphEdge(label = input, head = GraphNode(id = 1, isFinal = true)))

    assertEquals(
        expected = hashMapOf(0 to hashSetOf(1)),
        actual = GLL(nonterminalS, listOf(graph)).parse(),
    )
  }

  @Test
  fun `test 'dyck' hand-crafted grammar two cycle graph`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("("), nonterminalS, Terminal(")"))))
    nonterminalS.addAlternative(Alternative(listOf()))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    val graphNode3 = GraphNode(id = 3, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "(", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "(", head = graphNode2))
    graphNode2.addEdge(GraphEdge(label = "(", head = graphNode0))

    graphNode2.addEdge(GraphEdge(label = ")", head = graphNode3))
    graphNode3.addEdge(GraphEdge(label = ")", head = graphNode2))

    assertEquals(
        expected =
            hashMapOf(
                0 to hashSetOf(0, 2, 3),
                1 to hashSetOf(1, 2, 3),
                2 to hashSetOf(2, 3),
                3 to hashSetOf(3)),
        actual = GLL(nonterminalS, listOf(graphNode0, graphNode1, graphNode2, graphNode3)).parse())
  }

  @Test
  fun `test 'a-plus' hand-crafted grammar one cycle graph`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalS)))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

    assertEquals(
        expected =
            hashMapOf(
                0 to hashSetOf(1),
                1 to hashSetOf(1),
            ),
        actual = GLL(nonterminalS, listOf(graphNode0, graphNode1)).parse(),
    )
  }

  @Test
  fun `test 'a-star' hand-crafted grammar one cycle graph`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalS)))
    nonterminalS.addAlternative(Alternative(listOf()))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

    assertEquals(
        expected =
            hashMapOf(
                0 to hashSetOf(0, 1),
                1 to hashSetOf(1),
            ),
        actual = GLL(nonterminalS, listOf(graphNode0, graphNode1)).parse(),
    )
  }

  @Test
  fun `test 'g1' hand-crafted grammar two cycle graph`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(listOf(Terminal("subClassOf_r"), nonterminalS, Terminal("subClassOf"))))
    nonterminalS.addAlternative(
        Alternative(listOf(Terminal("subClassOf_r"), Terminal("subClassOf"))))
    nonterminalS.addAlternative(
        Alternative(listOf(Terminal("type_r"), nonterminalS, Terminal("type"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("type_r"), Terminal("type"))))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    val graphNode3 = GraphNode(id = 3, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "subClassOf_r", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "subClassOf_r", head = graphNode2))
    graphNode2.addEdge(GraphEdge(label = "subClassOf_r", head = graphNode0))

    graphNode2.addEdge(GraphEdge(label = "subClassOf", head = graphNode3))
    graphNode3.addEdge(GraphEdge(label = "subClassOf", head = graphNode2))

    assertEquals(
        expected =
            hashMapOf(
                0 to hashSetOf(2, 3),
                1 to hashSetOf(2, 3),
                2 to hashSetOf(2, 3),
            ),
        actual = GLL(nonterminalS, listOf(graphNode0, graphNode1, graphNode2, graphNode3)).parse(),
    )
  }
}
