package rsm.graphinput.withoutsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.graph.GraphEdge
import org.kotgll.graph.GraphNode
import org.kotgll.graph.readGraphFromString
import org.kotgll.rsm.grammar.RSMNonterminalEdge
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.RSMTerminalEdge
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.graphinput.withoutsppf.GLL
import kotlin.test.assertEquals

class TestRSMGraphInputWithoutSPPFFail {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0

    val graph = GraphNode(id = 0, isStart = true)

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(graph)).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(strings = ["", "b", "bb", "ab", "aa"])
  fun `test 'a' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head =
                RSMState(
                    id = 1,
                    nonterminal = nonterminalS,
                    isFinal = true,
                )))

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(strings = ["", "a", "b", "aba", "ababa", "aa", "b", "bb", "c", "cc"])
  fun `test 'ab' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
        )
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))
    rsmState1.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("b"),
            head =
                RSMState(
                    id = 2,
                    nonterminal = nonterminalS,
                    isFinal = true,
                )))

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(strings = ["b", "bb", "c", "cc", "ab", "ac"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))
    rsmState1.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(strings = ["", "b", "bb", "c", "cc", "ab", "ac"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))
    rsmState1.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(
      strings =
          [
              "abaa",
              "abba",
              "abca",
              "ababaa",
              "ababba",
              "ababca",
              "abbb",
              "abcb",
              "ababbb",
              "ababcb",
              "abac",
              "abbc",
              "abcc",
              "ababac",
              "ababbc",
              "ababcc",
          ])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("ab"),
            head = rsmState1,
        ))
    rsmState1.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("ab"),
            head = rsmState1,
        ))

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
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(graph)).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(
      strings =
          [
              "(",
              ")",
              "((",
              "))",
              "()(",
              "()()(",
              "()()()(",
              "())",
              "()())",
              "()()())",
              "(())(",
              "(())()(",
              "(())()()(",
              "(()))",
              "(())())",
              "(())()())",
              "(())(())(",
              "(())(())()(",
              "(())(())()()(",
              "(())(()))",
              "(())(())())",
              "(())(())()())",
              "(()())(()())(",
              "(()())(()()))",
          ])
  fun `test 'dyck' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
        )
    val rsmState3 =
        RSMState(
            id = 3,
            nonterminal = nonterminalS,
        )
    val rsmState4 =
        RSMState(
            id = 4,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("("),
            head = rsmState1,
        ))
    rsmState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState2,
        ))
    rsmState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal(")"),
            head = rsmState3,
        ))
    rsmState3.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState4,
        ))

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse(),
    )
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(
      strings =
          [
              "",
              "a",
              "b",
              "c",
              "d",
              "aa",
              "ac",
              "ad",
              "ba",
              "bb",
              "bc",
              "bd",
              "ca",
              "cb",
              "cc",
              "da",
              "db",
              "dc",
              "dd",
          ])
  fun `test 'ab or cd' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("ab"),
            head = rsmState1,
        ))
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("cd"),
            head = rsmState2,
        ))

    val graph = GraphNode(id = 0, isStart = true)
    graph.addEdge(
        GraphEdge(
            label = input,
            head = GraphNode(id = 1, isFinal = true),
        ))

    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graph)).parse())
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(strings = ["b", "bb", "ab"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))

    assertEquals(
        expected = hashMapOf(), actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(strings = ["", "a", "b", "c", "ab", "ac", "abb", "bc", "abcd"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
        )
    val rsmState3 =
        RSMState(
            id = 3,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState4 =
        RSMState(
            id = 4,
            nonterminal = nonterminalS,
        )
    val rsmState5 =
        RSMState(
            id = 5,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState6 =
        RSMState(
            id = 6,
            nonterminal = nonterminalA,
            isStart = true,
        )
    nonterminalA.startState = rsmState6
    val rsmState7 =
        RSMState(
            id = 7,
            nonterminal = nonterminalA,
        )
    val rsmState8 =
        RSMState(
            id = 8,
            nonterminal = nonterminalA,
            isFinal = true,
        )
    val rsmState9 =
        RSMState(
            id = 9,
            nonterminal = nonterminalB,
            isStart = true,
        )
    nonterminalB.startState = rsmState9
    val rsmState10 =
        RSMState(
            id = 10,
            nonterminal = nonterminalB,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))
    rsmState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalB,
            head = rsmState2,
        ))
    rsmState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("c"),
            head = rsmState3,
        ))
    rsmState0.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalA,
            head = rsmState4,
        ))
    rsmState4.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("c"),
            head = rsmState5,
        ))

    rsmState6.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState7,
        ))
    rsmState7.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("b"),
            head = rsmState8,
        ))

    rsmState9.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("b"),
            head = rsmState10,
        ))

    assertEquals(
        expected = hashMapOf(), actual = GLL(rsmState0, listOf(readGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be Empty for {0}")
  @ValueSource(
      strings =
          [
              "",
              "a",
              "b",
              "c",
              "d",
              "aa",
              "ac",
              "ad",
              "ba",
              "bb",
              "bc",
              "bd",
              "ca",
              "cb",
              "cc",
              "da",
              "db",
              "dc",
              "dd",
          ])
  fun `test 'ab or cd' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState3 =
        RSMState(
            id = 3,
            nonterminal = nonterminalA,
            isStart = true,
        )
    nonterminalA.startState = rsmState3
    val rsmState4 =
        RSMState(
            id = 4,
            nonterminal = nonterminalA,
            isFinal = true,
        )
    val rsmState5 =
        RSMState(
            id = 5,
            nonterminal = nonterminalA,
            isFinal = true,
        )
    val rsmState6 =
        RSMState(
            id = 6,
            nonterminal = nonterminalB,
            isStart = true,
        )
    nonterminalB.startState = rsmState6
    val rsmState7 = RSMState(id = 7, nonterminal = nonterminalB, isFinal = true)
    val rsmState8 =
        RSMState(
            id = 8,
            nonterminal = nonterminalB,
            isFinal = true,
        )

    rsmState0.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalA,
            head = rsmState1,
        ))
    rsmState0.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalB,
            head = rsmState2,
        ))
    rsmState3.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("ab"),
            head = rsmState4,
        ))
    rsmState3.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("cd"),
            head = rsmState5,
        ))
    rsmState6.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("ab"),
            head = rsmState7,
        ))
    rsmState6.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("cd"),
            head = rsmState8,
        ))

    val graphNode0 =
        GraphNode(
            id = 0,
            isStart = true,
        )
    val graphNode1 =
        GraphNode(
            id = 1,
            isFinal = true,
        )

    graphNode0.addEdge(
        GraphEdge(
            label = input,
            head = graphNode1,
        ))

    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graphNode0)).parse())
  }

  @Test
  fun `test 'dyck' hand-crafted grammar two cycle graph`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
        )
    val rsmState3 =
        RSMState(
            id = 3,
            nonterminal = nonterminalS,
        )
    val rsmState4 =
        RSMState(
            id = 4,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("("),
            head = rsmState1,
        ))
    rsmState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState2,
        ))
    rsmState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal(")"),
            head = rsmState3,
        ))
    rsmState3.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState4,
        ))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    val graphNode3 = GraphNode(id = 3, isStart = true)

    graphNode0.addEdge(GraphEdge(label = "(", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "(", head = graphNode2))
    graphNode2.addEdge(GraphEdge(label = "(", head = graphNode0))

    graphNode2.addEdge(GraphEdge(label = ")", head = graphNode3))
    graphNode3.addEdge(GraphEdge(label = ")", head = graphNode2))

    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graphNode3)).parse())
  }

  @Test
  fun `test 'a-plus' hand-crafted grammar one cycle graph`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))
    rsmState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState2,
        ))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true)

    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graphNode0)).parse())
    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graphNode1)).parse())
  }

  @Test
  fun `test 'a-star' hand-crafted grammar one cycle graph`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState0,
        ))

    val graphNode0 = GraphNode(id = 0, isStart = true)
    val graphNode1 = GraphNode(id = 1, isStart = true)

    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graphNode0)).parse())
    assertEquals(expected = hashMapOf(), actual = GLL(rsmState0, listOf(graphNode1)).parse())
  }

  @Test
  fun `test 'g1' hand-crafted grammar two cycle graph`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS)
    val rsmState2 = RSMState(id = 2, nonterminal = nonterminalS)
    val rsmState3 = RSMState(id = 3, nonterminal = nonterminalS, isFinal = true)
    val rsmState4 = RSMState(id = 4, nonterminal = nonterminalS)
    val rsmState5 = RSMState(id = 5, nonterminal = nonterminalS)
    val rsmState6 = RSMState(id = 6, nonterminal = nonterminalS, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState1))
    rsmState1.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState2))
    rsmState2.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState3))

    rsmState1.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState3))

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState4))
    rsmState4.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState5))
    rsmState5.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState6))

    rsmState4.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState6))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true)
    val graphNode3 = GraphNode(id = 3, isStart = true)

    graphNode0.addEdge(GraphEdge(label = "subClassOf_r", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "subClassOf_r", head = graphNode2))
    graphNode2.addEdge(GraphEdge(label = "subClassOf_r", head = graphNode0))

    graphNode2.addEdge(GraphEdge(label = "subClassOf", head = graphNode3))
    graphNode3.addEdge(GraphEdge(label = "subClassOf", head = graphNode2))

    assertEquals(
        expected = hashMapOf(),
        actual = GLL(rsmState0, listOf(graphNode0, graphNode1, graphNode2, graphNode3)).parse())
  }
}
