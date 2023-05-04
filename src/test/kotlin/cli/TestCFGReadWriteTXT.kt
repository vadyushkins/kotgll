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
        Alternative(listOf(Terminal("subClassOf_r"), nonterminalS, Terminal("subClassOf"))))
    nonterminalS.addAlternative(
        Alternative(listOf(Terminal("subClassOf_r"), Terminal("subClassOf"))))
    nonterminalS.addAlternative(
        Alternative(listOf(Terminal("type_r"), nonterminalS, Terminal("type"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("type_r"), Terminal("type"))))

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
        Alternative(listOf(Terminal("subClassOf_r"), nonterminalS, Terminal("subClassOf"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("subClassOf"))))

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

  @Test
  fun `'c_analysis' rsm`() {
    val nonterminalS = Nonterminal("S")
    val nonterminalV = Nonterminal("V")
    val nonterminalV1 = Nonterminal("V1")
    val nonterminalV2 = Nonterminal("V2")
    val nonterminalV3 = Nonterminal("V3")

    nonterminalS.addAlternative(Alternative(listOf(Terminal("d_r"), nonterminalV, Terminal("d"))))
    nonterminalV.addAlternative(Alternative(listOf(nonterminalV1, nonterminalV2, nonterminalV3)))
    nonterminalV1.addAlternative(Alternative(listOf()))
    nonterminalV1.addAlternative(Alternative(listOf(nonterminalV2, Terminal("a_r"), nonterminalV1)))
    nonterminalV2.addAlternative(Alternative(listOf()))
    nonterminalV2.addAlternative(Alternative(listOf(nonterminalS)))
    nonterminalV3.addAlternative(Alternative(listOf()))
    nonterminalV3.addAlternative(Alternative(listOf(Terminal("a"), nonterminalV2, nonterminalV3)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/c_analysis.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'reg1' cfg`() {
    val nonterminalS = Nonterminal("S")

    nonterminalS.addAlternative(Alternative(listOf()))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("type"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("subClassOf"))))
    nonterminalS.addAlternative(Alternative(listOf(nonterminalS, nonterminalS)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/reg1.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'reg2' cfg`() {
    val nonterminalS = Nonterminal("S")

    nonterminalS.addAlternative(Alternative(listOf()))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("type"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("type_r"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("subClassOf"))))
    nonterminalS.addAlternative(Alternative(listOf(Terminal("subClassOf_r"))))
    nonterminalS.addAlternative(Alternative(listOf(nonterminalS, nonterminalS)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/reg2.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'reg3' cfg`() {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")

    nonterminalS.addAlternative(Alternative(listOf(nonterminalA, nonterminalB)))

    nonterminalA.addAlternative(Alternative(listOf()))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("type"), nonterminalA)))

    nonterminalB.addAlternative(Alternative(listOf()))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("subClassOf"), nonterminalB)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/reg3.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }

  @Test
  fun `'reg5' cfg`() {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")
    val nonterminalC = Nonterminal("C")
    val nonterminalD = Nonterminal("D")

    nonterminalS.addAlternative(
        Alternative(listOf(nonterminalA, nonterminalB, nonterminalC, nonterminalD)))

    nonterminalA.addAlternative(Alternative(listOf()))
    nonterminalA.addAlternative(Alternative(listOf(Terminal("type"), nonterminalA)))

    nonterminalB.addAlternative(Alternative(listOf()))
    nonterminalB.addAlternative(Alternative(listOf(Terminal("subClassOf"), nonterminalB)))

    nonterminalC.addAlternative(Alternative(listOf()))
    nonterminalC.addAlternative(Alternative(listOf(Terminal("subClassOf_r"), nonterminalC)))

    nonterminalD.addAlternative(Alternative(listOf()))
    nonterminalD.addAlternative(Alternative(listOf(Terminal("type_r"), nonterminalD)))

    val pathToTXT = "src/test/resources/cli/TestCFGReadWriteTXT/reg4.txt"
    writeCFGToTXT(nonterminalS, pathToTXT)
    val actualNonterminal = readCFGFromTXT(pathToTXT)

    assertEquals(expected = nonterminalS, actual = actualNonterminal)
    assertEquals(expected = nonterminalS.alternatives, actual = actualNonterminal.alternatives)
  }
}
