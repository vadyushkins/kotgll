package cfg.stringinput.withsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.stringinput.withsppf.GLL
import kotlin.test.assertNotNull

class TestCFGStringInputWithSPPFSuccess {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf()))

    assertNotNull(GLL(nonterminalS, "").parse())
  }

  @Test
  fun `test 'a' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))

    assertNotNull(GLL(nonterminalS, "a").parse())
  }

  @Test
  fun `test 'ab' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("a"),
                Terminal("b"),
            )))

    assertNotNull(GLL(nonterminalS, "ab").parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a", "aa", "aaa", "aaaa"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("a")))))

    assertNotNull(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["a", "aa", "aaa"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Plus(Terminal("a")))))

    assertNotNull(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "ab", "abab", "ababab"])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("ab")))))

    assertNotNull(GLL(nonterminalS, input).parse())
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

    assertNotNull(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("cd"))))

    assertNotNull(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Optional(Terminal("a")))))

    assertNotNull(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["abc"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    nonterminalS.addAlternative(Alternative(listOf(nonterminalA, Terminal("c"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalB, Terminal("c"))))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("b"))))

    assertNotNull(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
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

    assertNotNull(GLL(nonterminalS, input).parse())
  }
}
