package rsm

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.rsm.GLL
import org.kotgll.rsm.grammar.RSMNonterminalEdge
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.RSMTerminalEdge
import org.kotgll.rsm.grammar.symbol.Literal
import org.kotgll.rsm.grammar.symbol.Nonterminal
import kotlin.test.assertNull

class TestHandCraftedFail {
    @Test
    fun `test 'empty' hand-crafted grammar`() {
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
            isFinal = true,
        )
        startNonterminal.startState = rsm

        assertNull(GLL(rsm, "a").parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["", "b", "bb"])
    fun `test 'a' hand-crafted grammar`(input: String) {
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
        )
        startNonterminal.startState = rsm
        rsm.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('a'),
                head = RSMState(
                    id = 1,
                    nonterminal = startNonterminal,
                    isFinal = true,
                )
            )
        )

        assertNull(GLL(rsm, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["", "a", "b", "aba", "ababa"])
    fun `test 'ab' hand-crafted grammar`(input: String) {
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
        )
        startNonterminal.startState = rsm
        val intermediateRSMState: RSMState = RSMState(
            id = 1,
            nonterminal = startNonterminal,
        )
        rsm.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('a'),
                head = intermediateRSMState,
            )
        )
        intermediateRSMState.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('b'),
                head = RSMState(
                    id = 2,
                    nonterminal = startNonterminal,
                    isFinal = true,
                )
            )
        )

        assertNull(GLL(rsm, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["b", "ab", "aab"])
    fun `test 'a-star' hand-crafted grammar`(input: String) {
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
            isFinal = true,
        )
        startNonterminal.startState = rsm
        val finalRSMState = RSMState(
            id = 1,
            nonterminal = startNonterminal,
            isFinal = true,
        )
        rsm.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('a'),
                head = finalRSMState,
            )
        )
        finalRSMState.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('a'),
                head = finalRSMState,
            )
        )

        assertNull(GLL(rsm, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["", "ab", "aab", "aaab"])
    fun `test 'a-plus' hand-crafted grammar`(input: String) {
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
        )
        startNonterminal.startState = rsm
        val finalRSMState = RSMState(
            id = 1,
            nonterminal = startNonterminal,
            isFinal = true,
        )
        rsm.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('a'),
                head = finalRSMState,
            )
        )
        finalRSMState.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('a'),
                head = finalRSMState,
            )
        )

        assertNull(GLL(rsm, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["aba", "ababa", "abababa"])
    fun `test '(ab)-star' hand-crafted grammar`(input: String) {
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
            isFinal = true,
        )
        startNonterminal.startState = rsm
        val finalRSMState = RSMState(
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

        assertNull(GLL(rsm, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
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
        val startNonterminal: Nonterminal = Nonterminal("S")
        val rsm: RSMState = RSMState(
            id = 0,
            nonterminal = startNonterminal,
            isStart = true,
            isFinal = true,
        )
        startNonterminal.startState = rsm
        val intermediateRSMState1: RSMState = RSMState(
            id = 1,
            nonterminal = startNonterminal,
        )
        val intermediateRSMState2: RSMState = RSMState(
            id = 2,
            nonterminal = startNonterminal,
        )
        val intermediateRSMState3: RSMState = RSMState(
            id = 3,
            nonterminal = startNonterminal,
        )
        val finalRSMState: RSMState = RSMState(
            id = 4,
            nonterminal = startNonterminal,
            isFinal = true,
        )

        rsm.addTerminalEdge(
            RSMTerminalEdge(
                terminal = org.kotgll.rsm.grammar.symbol.Char('('),
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
                terminal = org.kotgll.rsm.grammar.symbol.Char(')'),
                head = intermediateRSMState3,
            )
        )
        intermediateRSMState3.addNonterminalEdge(
            RSMNonterminalEdge(
                nonterminal = startNonterminal,
                head = finalRSMState,
            )
        )

        assertNull(GLL(rsm, input).parse())
    }
}