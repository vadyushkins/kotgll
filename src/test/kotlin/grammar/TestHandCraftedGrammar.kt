package grammar

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.Alternative
import org.kotgll.GLL
import org.kotgll.symbol.Char
import org.kotgll.symbol.Literal
import org.kotgll.symbol.Nonterminal
import org.kotgll.symbol.Star
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
        grammar.addAlternative(Alternative(listOf(Star(Char('a')))))

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
}