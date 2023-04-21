package cfg.stringinput.withoutsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.stringinput.withoutsppf.GLL
import kotlin.test.assertTrue

class TestCFGStringInputWithoutSPPFSuccess {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf()))

    assertTrue(GLL(nonterminalS, "").parse())
  }

  @Test
  fun `test 'a' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))

    assertTrue(GLL(nonterminalS, "a").parse())
  }

  @Test
  fun `test 'ab' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))

    assertTrue(GLL(nonterminalS, "ab").parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
  @ValueSource(strings = ["", "a", "aa", "aaa", "aaaa"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("a")))))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
  @ValueSource(strings = ["a", "aa", "aaa"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Plus(Terminal("a")))))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
  @ValueSource(strings = ["", "ab", "abab", "ababab"])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("ab")))))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
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
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("("),
                nonterminalS,
                Terminal(")"),
                nonterminalS,
            )))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("cd"))))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
  @ValueSource(strings = ["", "a"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Optional(Terminal("a")))))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
  @ValueSource(strings = ["abc"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    nonterminalS.addAlternative(Alternative(listOf(nonterminalA, Terminal("c"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalB, Terminal("c"))))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("b"))))

    assertTrue(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be True for {0}")
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

    assertTrue(GLL(nonterminalS, input).parse())
  }
}
