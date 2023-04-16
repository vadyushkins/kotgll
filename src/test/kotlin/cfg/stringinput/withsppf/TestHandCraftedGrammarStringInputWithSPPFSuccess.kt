package cfg.stringinput.withsppf

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.symbol.*
import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.stringinput.withsppf.GLL
import kotlin.test.assertNotNull

class TestHandCraftedGrammarStringInputWithSPPFSuccess {
  @Test
  fun `test 'empty' hand-crafted grammar`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf()))

    assertNotNull(GLL(grammar, "").parse())
  }

  @Test
  fun `test 'a' hand-crafted grammar`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Char('a'))))

    assertNotNull(GLL(grammar, "a").parse())
  }

  @Test
  fun `test 'ab' hand-crafted grammar`() {
    val grammar = Nonterminal("S")
    grammar.addAlternative(
        Alternative(
            listOf(
                Char('a'),
                Char('b'),
            )))

    assertNotNull(GLL(grammar, "ab").parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a", "aa", "aaa"])
  fun `test 'a-star' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Star(Char('a')))))

    assertNotNull(GLL(grammar, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["a", "aa", "aaa"])
  fun `test 'a-plus' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Plus(Char('a')))))

    assertNotNull(GLL(grammar, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "ab", "abab", "ababab"])
  fun `test '(ab)-star' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Star(Literal("ab")))))

    assertNotNull(GLL(grammar, input).parse())
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
    grammar.addAlternative(
        Alternative(
            listOf(
                Char('('),
                grammar,
                Char(')'),
                grammar,
            )))

    assertNotNull(GLL(grammar, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["ab", "cd"])
  fun `test 'ab or cd' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Literal("ab"))))
    grammar.addAlternative(Alternative(listOf(Literal("cd"))))

    assertNotNull(GLL(grammar, input).parse())
  }

  @ParameterizedTest(name = "Should be NotNull for {0}")
  @ValueSource(strings = ["", "a"])
  fun `test 'a-optional' hand-crafted grammar`(input: String) {
    val grammar = Nonterminal("S")
    grammar.addAlternative(Alternative(listOf(Optional(Char('a')))))

    assertNotNull(GLL(grammar, input).parse())
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

    assertNotNull(GLL(grammar, input).parse())
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

    assertNotNull(GLL(grammar, input).parse())
  }
}
