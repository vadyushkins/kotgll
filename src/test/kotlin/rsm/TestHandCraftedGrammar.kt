package rsm

import org.junit.jupiter.api.Test
import org.kotgll.rsm.GLL
import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.grammar.RSMTerminalEdge
import org.kotgll.rsm.grammar.symbol.Char
import org.kotgll.rsm.grammar.symbol.Nonterminal
import kotlin.test.assertNotNull

class TestHandCraftedGrammar {
    @Test
    fun `test 'empty' hand-crafted grammar`() {
        val rsm: RSMState = RSMState(
            id = 0,
            isStartFor = listOf(Nonterminal("S")),
            isFinalFor = listOf(Nonterminal("S")),
        )

        assertNotNull(GLL(rsm, "").parse())
    }

    @Test
    fun `test 'a' hand-crafted grammar`() {
        val rsm: RSMState = RSMState(
            id = 0,
            isStartFor = listOf(Nonterminal("S")),
        )
        rsm.addTerminalEdge(RSMTerminalEdge(
            terminal = Char('a'),
            head = RSMState(
                id = 1,
                isFinalFor = listOf(Nonterminal("S")),
            )
        ))

        assertNotNull(GLL(rsm, "a").parse())
    }
}