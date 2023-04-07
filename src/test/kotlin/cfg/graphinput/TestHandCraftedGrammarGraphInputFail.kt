package cfg.graphinput

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.graphinput.GLL
import org.kotgll.cfg.graphinput.graph.GraphEdge
import org.kotgll.cfg.graphinput.graph.GraphNode
import org.kotgll.cfg.graphinput.graph.makeGraphFromString
import kotlin.test.assertNull

class TestHandCraftedGrammarGraphInputFail {
    @Test
    fun `test 'empty' hand-crafted grammar`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))

        val graph = GraphNode(id = 0)

        assertNull(GLL(grammar, listOf(graph)).parse())
    }

    @Test
    fun `test 'a' hand-crafted grammar`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Char('a'))))

        assertNull(GLL(grammar, listOf(makeGraphFromString("b"))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["", "a", "aa", "b", "bb", "c", "cc"])
    fun `test 'ab' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Char('a'), Char('b'))))

        assertNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["b", "bb", "bbb"])
    fun `test 'a-star' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Star(Char('a')))))

        assertNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["", "b", "bb", "bbb"])
    fun `test 'a-plus' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Plus(Char('a')))))

        assertNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["abac", "ababac", "abababac", "cc"])
    fun `test '(ab)-star' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Star(Literal("ab")))))

        val graph = GraphNode(id = 0, isStart = true)
        var cur = graph
        var i = 0
        while (i < input.length) {
            cur.addEdge(
                GraphEdge(
                    label = "" + input[i] + input[i + 1],
                    head = GraphNode(id = i + 1)
                )
            )
            cur = cur.outgoingEdges[0].head
            i += 2
        }
        cur.isFinal = true

        assertNull(GLL(grammar, listOf(graph)).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(
        strings = [
            "()(", "()()(", "()()()(",
            "())", "()())", "()()())",
            "(())(", "(())()(", "(())()()(",
            "(()))", "(())())", "(())()())",
            "(())(())(", "(())(())()(", "(())(())()()(",
            "(())(()))", "(())(())())", "(())(())()())",
            "(()())(()())(",
            "(()())(()()))",
        ]
    )
    fun `test 'dyck' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))
        grammar.addAlternative(Alternative(listOf(Char('('), grammar, Char(')'), grammar)))

        assertNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["ac", "bd", "ef"])
    fun `test 'ab or cd' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Literal("ab"))))
        grammar.addAlternative(Alternative(listOf(Literal("cd"))))

        val graph = GraphNode(id = 0, isStart = true)
        graph.addEdge(
            GraphEdge(
                label = input,
                head = GraphNode(id = 1, isFinal = true)
            )
        )

        assertNull(GLL(grammar, listOf(graph)).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["b", "bb"])
    fun `test 'a-optional' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Optional(Char('a')))))

        assertNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(strings = ["", "a", "b", "c", "ab", "ac", "abb", "bc"])
    fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        val nonterminalA = Nonterminal("A")
        val nonterminalB = Nonterminal("B")

        grammar.addAlternative(Alternative(listOf(nonterminalA, Char('c'))))
        grammar.addAlternative(Alternative(listOf(Char('a'), nonterminalB, Char('c'))))
        nonterminalA.addAlternative(Alternative(listOf(Char('a'), Char('b'))))
        nonterminalB.addAlternative(Alternative(listOf(Char('b'))))

        assertNull(GLL(grammar, listOf(makeGraphFromString(input))).parse())
    }

    @ParameterizedTest(name = "Should be Null for {0}")
    @ValueSource(
        strings = [
            "", "a", "b", "c", "d",
            "aa", "ac", "ad",
            "ba", "bb", "bc", "bd",
            "ca", "cb", "cc",
            "da", "db", "dc", "dd",
        ]
    )
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
        graph.addEdge(
            GraphEdge(
                label = input,
                head = GraphNode(id = 1, isFinal = true)
            )
        )

        assertNull(GLL(grammar, listOf(graph)).parse())
    }

    @Test
    fun `test 'dyck' hand-crafted grammar two cycle graph`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))
        grammar.addAlternative(Alternative(listOf(Char('('), grammar, Char(')'), grammar)))

        val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
        val graphNode1 = GraphNode(id = 1, isStart = true, isFinal = true)
        val graphNode2 = GraphNode(id = 2, isStart = true, isFinal = true)
        val graphNode3 = GraphNode(id = 3, isStart = true)

        graphNode0.addEdge(GraphEdge(label = "(", head = graphNode1))
        graphNode1.addEdge(GraphEdge(label = "(", head = graphNode2))
        graphNode2.addEdge(GraphEdge(label = "(", head = graphNode0))

        graphNode2.addEdge(GraphEdge(label = ")", head = graphNode3))
        graphNode3.addEdge(GraphEdge(label = ")", head = graphNode2))

        assertNull(GLL(grammar, listOf(graphNode3)).parse())
    }

    @Test
    fun `test 'a-plus' hand-crafted grammar one cycle graph`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Char('a'))))
        grammar.addAlternative(Alternative(listOf(Char('a'), grammar)))

        val graphNode0 = GraphNode(id = 0, isStart = true, isFinal = true)
        val graphNode1 = GraphNode(id = 1, isStart = true)

        graphNode0.addEdge(GraphEdge(label = "a", head = graphNode1))
        graphNode1.addEdge(GraphEdge(label = "a", head = graphNode1))

        assertNull(GLL(grammar, listOf(graphNode0)).parse())
        assertNull(GLL(grammar, listOf(graphNode1)).parse())
    }
}