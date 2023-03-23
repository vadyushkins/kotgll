package grammar

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.GLL
import org.kotgll.grammar.Alternative
import org.kotgll.grammar.symbol.*
import org.kotgll.grammar.symbol.Char
import kotlin.test.assertNotNull

class TestHandCraftedGrammar {
    @Test
    fun `test 'empty' hand-crafted grammar`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))

        assertNotNull(GLL(grammar, "").parse())
    }

    @Test
    fun `test 'ab' hand-crafted grammar`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(
            Alternative(
                listOf(
                    Char('a'), Char('b'),
                )
            )
        )

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
    @ValueSource(strings = ["", "()", "()()", "(()())(()())"])
    fun `test 'dyck' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))
        grammar.addAlternative(
            Alternative(
                listOf(
                    Char('('), grammar, Char(')'), grammar,
                )
            )
        )

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

    @ParameterizedTest(name = "Should be NotNull for ({0})")
    @ValueSource(strings = ["a", "b", "c", "abc"]) // TODO: Fix for ""
    fun `test 'regular expression' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(
            Alternative(
                listOf(
                    RegularExpression("(a|b|c)*")
                )
            )
        )

        assertNotNull(GLL(grammar, input).parse())
    }
}