package cli

import org.junit.jupiter.api.Test
import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.grammar.readCFGFromTXT
import org.kotgll.cfg.grammar.symbol.Nonterminal
import org.kotgll.cfg.grammar.symbol.Terminal
import org.kotgll.cfg.grammar.writeCFGToTXT
import kotlin.test.assertEquals

class TestCFGReadWriteTXT {
  @Test
  fun `'a' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/a.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'a-star' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("a"), nonterminalS)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/a_star.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'dyck' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(listOf(Terminal("("), nonterminalS, Terminal(")"), nonterminalS)))
    nonterminalS.addAlternative(Alternative(listOf()))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/dyck.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'g1' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("subClassOf_r"),
                nonterminalS,
                Terminal("subClassOf"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("subClassOf_r"),
                Terminal("subClassOf"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("type_r"),
                nonterminalS,
                Terminal("type"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("type_r"),
                Terminal("type"),
            )))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/g1.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'g2' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("subClassOf_r"),
                nonterminalS,
                Terminal("subClassOf"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("subClassOf"),
            )))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/g2.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'geo' cfg`() {
    val nonterminalS = Nonterminal("S")
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("broaderTransitive"),
                nonterminalS,
                Terminal("broaderTransitive_r"),
            )))
    nonterminalS.addAlternative(
        Alternative(
            listOf(
                Terminal("broaderTransitive"),
                Terminal("broaderTransitive_r"),
            )))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/geo.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }
}
