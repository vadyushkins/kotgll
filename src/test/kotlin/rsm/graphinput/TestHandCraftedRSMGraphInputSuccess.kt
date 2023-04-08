package rsm.graphinput

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.rsm.grammar.RSMNonterminalEdge
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.RSMTerminalEdge
import org.kotgll.rsm.grammar.symbol.Char
import org.kotgll.rsm.grammar.symbol.Literal
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.graphinput.GLL
import org.kotgll.rsm.graphinput.graph.GraphEdge
import org.kotgll.rsm.graphinput.graph.GraphNode
import org.kotgll.rsm.graphinput.graph.makeGraphFromString
import kotlin.test.assertNotNull

class TestHandCraftedRSMGraphInputSuccess {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
        isFinal = true,
      )
    startNonterminal.startState = rsm

    val graph = GraphNode(id = 0, isStart = true, isFinal = true)

    assertNotNull(GLL(rsm, listOf(graph)).parse())
  }

  @Test
  fun `test 'a' hand-crafted grammar`() {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
      )
    startNonterminal.startState = rsm
    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head =
          RSMState(
            id = 1,
            nonterminal = startNonterminal,
            isFinal = true,
          )
      )
    )

    assertNotNull(GLL(rsm, listOf(makeGraphFromString("a"))).parse())
  }

  @Test
  fun `test 'ab' hand-crafted grammar`() {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
      )
    startNonterminal.startState = rsm
    val intermediateRSMState =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
      )
    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = intermediateRSMState,
      )
    )
    intermediateRSMState.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('b'),
        head =
          RSMState(
            id = 2,
            nonterminal = startNonterminal,
            isFinal = true,
          )
      )
    )

    assertNotNull(GLL(rsm, listOf(makeGraphFromString("ab"))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a", "aa", "aaa"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
        isFinal = true,
      )
    startNonterminal.startState = rsm
    val finalRSMState =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
        isFinal = true,
      )
    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = finalRSMState,
      )
    )
    finalRSMState.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = finalRSMState,
      )
    )

    assertNotNull(GLL(rsm, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["a", "aa", "aaa"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
      )
    startNonterminal.startState = rsm
    val finalRSMState =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
        isFinal = true,
      )
    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = finalRSMState,
      )
    )
    finalRSMState.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = finalRSMState,
      )
    )

    assertNotNull(GLL(rsm, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "ab", "abab", "ababab"])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
        isFinal = true,
      )
    startNonterminal.startState = rsm
    val finalRSMState =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
        isFinal = true,
      )
    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Literal("ab"),
        head = finalRSMState,
      )
    )
    finalRSMState.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Literal("ab"),
        head = finalRSMState,
      )
    )

    val graph = GraphNode(id = 0, isStart = true)
    var cur = graph
    var i = 0
    while (i < input.length) {
      cur.addEdge(GraphEdge(label = "" + input[i] + input[i + 1], head = GraphNode(id = i + 1)))
      cur = cur.outgoingEdges[0].head
      i += 2
    }
    cur.isFinal = true

    assertNotNull(GLL(rsm, listOf(graph)).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
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
      ]
  )
  fun `test 'dyck' hand-crafted grammar`(input: String) {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
        isFinal = true,
      )
    startNonterminal.startState = rsm
    val intermediateRSMState1 =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState2 =
      RSMState(
        id = 2,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState3 =
      RSMState(
        id = 3,
        nonterminal = startNonterminal,
      )
    val finalRSMState =
      RSMState(
        id = 4,
        nonterminal = startNonterminal,
        isFinal = true,
      )

    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('('),
        head = intermediateRSMState1,
      )
    )
    intermediateRSMState1.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = startNonterminal,
        head = intermediateRSMState2,
      )
    )
    intermediateRSMState2.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char(')'),
        head = intermediateRSMState3,
      )
    )
    intermediateRSMState3.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = startNonterminal,
        head = finalRSMState,
      )
    )

    assertNotNull(GLL(rsm, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["abc"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val startNonterminal = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
      )
    startNonterminal.startState = rsm
    val intermediateRSMState1 =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState2 =
      RSMState(
        id = 2,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState3 =
      RSMState(
        id = 3,
        nonterminal = startNonterminal,
        isFinal = true,
      )
    val intermediateRSMState4 =
      RSMState(
        id = 4,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState5 =
      RSMState(
        id = 5,
        nonterminal = startNonterminal,
        isFinal = true,
      )
    val intermediateRSMState6 =
      RSMState(
        id = 6,
        nonterminal = nonterminalA,
        isStart = true,
      )
    nonterminalA.startState = intermediateRSMState6
    val intermediateRSMState7 =
      RSMState(
        id = 7,
        nonterminal = nonterminalA,
      )
    val intermediateRSMState8 =
      RSMState(
        id = 8,
        nonterminal = nonterminalA,
        isFinal = true,
      )
    val intermediateRSMState9 =
      RSMState(
        id = 9,
        nonterminal = nonterminalB,
        isStart = true,
      )
    nonterminalB.startState = intermediateRSMState9
    val intermediateRSMState10 =
      RSMState(
        id = 10,
        nonterminal = nonterminalB,
        isFinal = true,
      )

    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = intermediateRSMState1,
      )
    )
    intermediateRSMState1.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = nonterminalB,
        head = intermediateRSMState2,
      )
    )
    intermediateRSMState2.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('c'),
        head = intermediateRSMState3,
      )
    )
    rsm.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = nonterminalA,
        head = intermediateRSMState4,
      )
    )
    intermediateRSMState4.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('c'),
        head = intermediateRSMState5,
      )
    )

    intermediateRSMState6.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('a'),
        head = intermediateRSMState7,
      )
    )
    intermediateRSMState7.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('b'),
        head = intermediateRSMState8,
      )
    )

    intermediateRSMState9.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('b'),
        head = intermediateRSMState10,
      )
    )

    assertNotNull(GLL(rsm, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["ab", "cd"])
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
      )
    )
    rsmState0.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = nonterminalB,
        head = rsmState2,
      )
    )
    rsmState3.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Literal("ab"),
        head = rsmState4,
      )
    )
    rsmState3.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Literal("cd"),
        head = rsmState5,
      )
    )
    rsmState6.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Literal("ab"),
        head = rsmState7,
      )
    )
    rsmState6.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Literal("cd"),
        head = rsmState8,
      )
    )

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
      )
    )

    assertNotNull(GLL(rsmState0, listOf(graphNode0)).parse())
  }

  @Test
  fun `test 'dyck' hand-crafted grammar two cycle graph`() {
    val startNonterminal = Nonterminal("S")
    val rsm =
      RSMState(
        id = 0,
        nonterminal = startNonterminal,
        isStart = true,
        isFinal = true,
      )
    startNonterminal.startState = rsm
    val intermediateRSMState1 =
      RSMState(
        id = 1,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState2 =
      RSMState(
        id = 2,
        nonterminal = startNonterminal,
      )
    val intermediateRSMState3 =
      RSMState(
        id = 3,
        nonterminal = startNonterminal,
      )
    val finalRSMState =
      RSMState(
        id = 4,
        nonterminal = startNonterminal,
        isFinal = true,
      )

    rsm.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char('('),
        head = intermediateRSMState1,
      )
    )
    intermediateRSMState1.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = startNonterminal,
        head = intermediateRSMState2,
      )
    )
    intermediateRSMState2.addTerminalEdge(
      RSMTerminalEdge(
        terminal = Char(')'),
        head = intermediateRSMState3,
      )
    )
    intermediateRSMState3.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = startNonterminal,
        head = finalRSMState,
      )
    )

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    val graphNode3 = GraphNode(id = 3, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "(", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "(", head = graphNode2))
    graphNode2.addEdge(GraphEdge(label = "(", head = graphNode0))

    graphNode2.addEdge(GraphEdge(label = ")", head = graphNode3))
    graphNode3.addEdge(GraphEdge(label = ")", head = graphNode2))

    assertNotNull(GLL(rsm, listOf(graphNode0)).parse())
    assertNotNull(GLL(rsm, listOf(graphNode1)).parse())
    assertNotNull(GLL(rsm, listOf(graphNode2)).parse())
    assertNotNull(GLL(rsm, listOf(graphNode3)).parse())
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
        terminal = Char('a'),
        head = rsmState1,
      )
    )
    rsmState1.addNonterminalEdge(
      RSMNonterminalEdge(
        nonterminal = nonterminalS,
        head = rsmState2,
      )
    )

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

    assertNotNull(GLL(rsmState0, listOf(graphNode0)).parse())
    assertNotNull(GLL(rsmState0, listOf(graphNode1)).parse())
  }
}
