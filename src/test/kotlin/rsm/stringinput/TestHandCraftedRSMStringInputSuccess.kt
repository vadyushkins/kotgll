package rsm.stringinput

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.rsm.grammar.RSMNonterminalEdge
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.RSMTerminalEdge
import org.kotgll.rsm.grammar.symbol.Char
import org.kotgll.rsm.grammar.symbol.Literal
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.stringinput.GLL
import kotlin.test.assertNotNull

class TestHandCraftedRSMStringInputSuccess {
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

    assertNotNull(GLL(rsm, "").parse())
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
                )))

    assertNotNull(GLL(rsm, "a").parse())
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
        ))
    intermediateRSMState.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('b'),
            head =
                RSMState(
                    id = 2,
                    nonterminal = startNonterminal,
                    isFinal = true,
                )))

    assertNotNull(GLL(rsm, "ab").parse())
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
        ))
    finalRSMState.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('a'),
            head = finalRSMState,
        ))

    assertNotNull(GLL(rsm, input).parse())
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
        ))
    finalRSMState.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('a'),
            head = finalRSMState,
        ))

    assertNotNull(GLL(rsm, input).parse())
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
        ))
    finalRSMState.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Literal("ab"),
            head = finalRSMState,
        ))

    assertNotNull(GLL(rsm, input).parse())
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
        ))
    intermediateRSMState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = startNonterminal,
            head = intermediateRSMState2,
        ))
    intermediateRSMState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char(')'),
            head = intermediateRSMState3,
        ))
    intermediateRSMState3.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = startNonterminal,
            head = finalRSMState,
        ))

    assertNotNull(GLL(rsm, input).parse())
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
        ))
    intermediateRSMState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalB,
            head = intermediateRSMState2,
        ))
    intermediateRSMState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('c'),
            head = intermediateRSMState3,
        ))
    rsm.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalA,
            head = intermediateRSMState4,
        ))
    intermediateRSMState4.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('c'),
            head = intermediateRSMState5,
        ))

    intermediateRSMState6.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('a'),
            head = intermediateRSMState7,
        ))
    intermediateRSMState7.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('b'),
            head = intermediateRSMState8,
        ))

    intermediateRSMState9.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Char('b'),
            head = intermediateRSMState10,
        ))

    assertNotNull(GLL(rsm, input).parse())
  }
}
