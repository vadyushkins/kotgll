package vanilla

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kotgll.vanilla.GLL
import org.kotgll.vanilla.grammar.Alternative
import org.kotgll.vanilla.grammar.symbol.*
import org.kotgll.vanilla.grammar.symbol.Char
import kotlin.test.assertNull

class TestHandCraftedFail {
    @Test
    fun `test 'empty' hand-crafted grammar`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))

        assertNull(GLL(grammar, "a").parse())
    }

    @Test
    fun `test 'a' hand-crafted grammar`() {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Char('a'))))

        assertNull(GLL(grammar, "b").parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["", "a", "aa", "b", "bb"])
    fun `test 'ab' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(
            Alternative(
                listOf(
                    Char('a'), Char('b'),
                )
            )
        )

        assertNull(GLL(grammar, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["b", "bb", "bbb"])
    fun `test 'a-star' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Star(Char('a')))))

        assertNull(GLL(grammar, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["", "b", "bb", "bbb"])
    fun `test 'a-plus' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Plus(Char('a')))))

        assertNull(GLL(grammar, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["aba", "ababa", "abababa"])
    fun `test '(ab)-star' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Star(Literal("ab")))))

        assertNull(GLL(grammar, input).parse())
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
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf()))
        grammar.addAlternative(
            Alternative(
                listOf(
                    Char('('), grammar,
                    Char(')'), grammar,
                )
            )
        )

        assertNull(GLL(grammar, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["ac", "bd"])
    fun `test 'ab or cd' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Literal("ab"))))
        grammar.addAlternative(Alternative(listOf(Literal("cd"))))

        assertNull(GLL(grammar, input).parse())
    }

    @ParameterizedTest(name = "Should be NotNull for {0}")
    @ValueSource(strings = ["b", "bb"])
    fun `test 'a-optional' hand-crafted grammar`(input: String) {
        val grammar = Nonterminal("S")
        grammar.addAlternative(Alternative(listOf(Optional(Char('a')))))

        assertNull(GLL(grammar, input).parse())
    }
}