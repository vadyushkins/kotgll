package cfg.stringinput.withoutsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.stringinput.withoutsppf.GLL
import kotlin.test.assertFalse

class TestCFGStringInputWithoutSPPFFail {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf()))

    assertFalse(GLL(nonterminalS, "a").parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
  @ValueSource(strings = ["", "b", "bb", "ab", "aa"])
  fun `test 'a' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be False for {0}")
  @ValueSource(strings = ["", "a", "b", "aba", "ababa", "aa", "b", "bb", "c", "cc"])
  fun `test 'ab' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
  @ValueSource(strings = ["b", "bb", "c", "cc", "ab", "ac"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("a")))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
  @ValueSource(strings = ["", "b", "bb", "c", "cc", "ab", "ac"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Plus(Terminal("a")))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
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
    nonterminalS.addAlternative(Alternative(listOf(Star(Terminal("ab")))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
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
    nonterminalS.addAlternative(Alternative(listOf()))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("("),
                nonterminalS,
                Terminal(")"),
                nonterminalS,
            )))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
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
    nonterminalS.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("cd"))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
  @ValueSource(strings = ["b", "bb", "ab"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Optional(Terminal("a")))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
  @ValueSource(strings = ["", "a", "b", "c", "ab", "ac", "abb", "bc", "abcd"])
  fun `test 'abc' ambiguous hand-crafted grammar`(input: String) {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    nonterminalS.addAlternative(Alternative(listOf(nonterminalA, Terminal("c"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalB, Terminal("c"))))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("a"), Terminal("b"))))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("b"))))

    assertFalse(GLL(nonterminalS, input).parse())
  }

  @ParameterizedTest(name = "Should be Null for {0}")
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
    nonterminalS.addAlternative(Alternative(listOf(nonterminalA)))
    nonterminalS.addAlternative(Alternative(listOf(nonterminalB)))

    nonterminalA.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("cd"))))

    nonterminalB.addAlternative(Alternative(listOf(Terminal("ab"))))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("cd"))))

    assertFalse(GLL(nonterminalS, input).parse())
  }
}
