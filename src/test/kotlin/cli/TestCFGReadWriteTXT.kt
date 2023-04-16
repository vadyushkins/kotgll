package cli

import org.junit.jupiter.api.Test
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.readCFGFromText
import org.kotgll.cfg.grammar.symbol.Char
import org.kotgll.cfg.grammar.symbol.Literal
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.writeCFGToTXT
import java.io.File
import kotlin.test.assertEquals

class TestCFGReadWriteTXT {
  @Test
  fun `'a' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Char('a'))))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/a.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'a-star' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Char('a'))))
    nonterminalS.addAlternative(Alternative(listOf(Char('a'), nonterminalS)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/a_star.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'dyck' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(listOf(Char('('), nonterminalS, Char(')'), nonterminalS)))
    nonterminalS.addAlternative(Alternative(listOf()))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/dyck.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'g1' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("subClassOf_r"),
                nonterminalS,
                Literal("subClassOf"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("subClassOf_r"),
                Literal("subClassOf"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("type_r"),
                nonterminalS,
                Literal("type"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("type_r"),
                Literal("type"),
            )))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/g1.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'g2' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("subClassOf_r"),
                nonterminalS,
                Literal("subClassOf"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("subClassOf"),
            )))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/g2.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'geo' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("broaderTransitive"),
                nonterminalS,
                Literal("broaderTransitive_r"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Literal("broaderTransitive"),
                Literal("broaderTransitive_r"),
            )))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/geo.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromText(File(pathToTXT).inputStream())

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }
}
