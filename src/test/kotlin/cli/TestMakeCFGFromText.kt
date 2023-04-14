package cli

import org.junit.jupiter.api.Test
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.makeCFGFromText
import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.grammar.symbol.Nonterminal
import java.io.File
import kotlin.test.assertEquals

class TestMakeCFGFromText {
  @Test
  fun `make 'a' cfg from txt`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Char('a'))))

    val pathToTXT = "src/test/resources/cli/TestMakeCFGFromText/a.txt"
    val actualNonterminal = makeCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `make 'a-star' cfg from txt`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Char('a'))))
    nonterminalS.addAlternative(Alternative(listOf(Char('a'), nonterminalS)))

    val pathToTXT = "src/test/resources/cli/TestMakeCFGFromText/a_star.txt"
    val actualNonterminal = makeCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `make 'dyck' cfg from txt`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Char('('), nonterminalS, Char(')'), nonterminalS)))
    nonterminalS.addAlternative(Alternative(listOf()))

    val pathToTXT = "src/test/resources/cli/TestMakeCFGFromText/dyck.txt"
    val actualNonterminal = makeCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }
}