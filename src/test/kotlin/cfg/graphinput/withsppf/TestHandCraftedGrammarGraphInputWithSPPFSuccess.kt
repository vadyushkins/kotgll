package cfg.graphinput.withsppf

import makeGraphFromString
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.graphinput.withsppf.GLL
import org.kotgll.graph.GraphEdge
import org.kotgll.graph.GraphNode
import kotlin.test.assertNotNull

class TestHandCraftedGrammarGraphInputWithSPPFSuccess {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf()))

    val graph = GraphNode(id = 0, isStart = true, isFinal = true)

    assertNotNull(GLL(grammar, listOf(graph)).parse())
  }

  @Test
  fun `test 'a' hand-crafted grammar`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Char('a'))))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString("a"))).parse())
  }

  @Test
  fun `test 'ab' hand-crafted grammar`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Char('a'), Char('b'))))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString("ab"))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a", "aa", "aaa"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Star(Char('a')))))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["a", "aa", "aaa"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Plus(Char('a')))))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "ab", "abab", "ababab"])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Star(Literal("ab")))))

    val graph = GraphNode(id = 0, isStart = true)
    var cur = graph
    var i = 0
    while (i < input.length) {
      cur.addEdge(GraphEdge(label = "" + input[i] + input[i + 1], head = GraphNode(id = i + 1)))
      cur = cur.outgoingEdges[0].head
      i += 2
    }
    cur.isFinal = true

    assertNotNull(GLL(grammar, listOf(graph)).parse())
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
          ])
  fun `test 'dyck' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf()))
    grammar.addAlternative(Alternative(listOf(Char('('), grammar, Char(')'), grammar)))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Literal("ab"))))
    grammar.addAlternative(Alternative(listOf(Literal("cd"))))

    val graph = GraphNode(id = 0, isStart = true)
    graph.addEdge(GraphEdge(label = input, head = GraphNode(id = 1, isFinal = true)))

    assertNotNull(GLL(grammar, listOf(graph)).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Optional(Char('a')))))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["abc"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    grammar.addAlternative(Alternative(listOf(nonterminalA, Char('c'))))
    grammar.addAlternative(Alternative(listOf(Char('a'), nonterminalB, Char('c'))))
    nonterminalA.addAlternative(Alternative(listOf(Char('a'), Char('b'))))
    nonterminalB.addAlternative(Alternative(listOf(Char('b'))))

    assertNotNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' ambiguous hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")
    grammar.addAlternative(Alternative(listOf(nonterminalA)))
    grammar.addAlternative(Alternative(listOf(nonterminalB)))

    nonterminalA.addAlternative(Alternative(listOf(Literal("ab"))))
    nonterminalA.addAlternative(Alternative(listOf(Literal("cd"))))

    nonterminalB.addAlternative(Alternative(listOf(Literal("ab"))))
    nonterminalB.addAlternative(Alternative(listOf(Literal("cd"))))

    val graph = GraphNode(id = 0, isStart = true)
    graph.addEdge(GraphEdge(label = input, head = GraphNode(id = 1, isFinal = true)))

    assertNotNull(GLL(grammar, listOf(graph)).parse())
  }

  @Test
  fun `test 'dyck' hand-crafted grammar two cycle graph`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf()))
    grammar.addAlternative(Alternative(listOf(Char('('), grammar, Char(')'), grammar)))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
    val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
    val graphNode3 = GraphNode(id = 3, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "(", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "(", head = graphNode2))
    graphNode2.addEdge(GraphEdge(label = "(", head = graphNode0))

    graphNode2.addEdge(GraphEdge(label = ")", head = graphNode3))
    graphNode3.addEdge(GraphEdge(label = ")", head = graphNode2))

    assertNotNull(GLL(grammar, listOf(graphNode0)).parse())
    assertNotNull(GLL(grammar, listOf(graphNode1)).parse())
    assertNotNull(GLL(grammar, listOf(graphNode2)).parse())
    assertNotNull(GLL(grammar, listOf(graphNode3)).parse())
  }

  @Test
  fun `test 'a-plus' hand-crafted grammar one cycle graph`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Char('a'))))
    grammar.addAlternative(Alternative(listOf(Char('a'), grammar)))

    val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
    val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)

    graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
    graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

    assertNotNull(GLL(grammar, listOf(graphNode0)).parse())
    assertNotNull(GLL(grammar, listOf(graphNode1)).parse())
  }
}
