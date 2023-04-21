package rsm.stringinput.withoutsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.rsm.grammar.RSMNonterminalEdge
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.RSMTerminalEdge
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import org.kotgll.rsm.stringinput.withoutsppf.GLL
import kotlin.test.assertFalse

class TestRSMStringInputWithoutSPPFFail {
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

    assertFalse(GLL(rsmState0, "a").parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("ab"), head = rsmState1))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("cd"), head = rsmState1))

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState1))

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
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

    assertFalse(GLL(rsmState0, input).parse())
  }
}
