package cli

import org.junit.jupiter.api.Test
import org.kotgll.rsm.grammar.*
import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import kotlin.test.assertEquals

class TestRSMReadWriteTXT {
  @Test
  fun `'a' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState1))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/a.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'a-star' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState1))
    rsmState1.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState1))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/a_star.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'dyck' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
            isFinal = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
        )
    val rsmState3 =
        RSMState(
            id = 3,
            nonterminal = nonterminalS,
        )
    val rsmState4 =
        RSMState(
            id = 4,
            nonterminal = nonterminalS,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("("),
            head = rsmState1,
        ))
    rsmState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState2,
        ))
    rsmState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal(")"),
            head = rsmState3,
        ))
    rsmState3.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalS,
            head = rsmState4,
        ))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/dyck.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'abc' rsm`() {
    val nonterminalS = Nonterminal("S")
    val nonterminalA = Nonterminal("A")
    val nonterminalB = Nonterminal("B")
    val rsmState0 =
        RSMState(
            id = 0,
            nonterminal = nonterminalS,
            isStart = true,
        )
    nonterminalS.startState = rsmState0
    val rsmState1 =
        RSMState(
            id = 1,
            nonterminal = nonterminalS,
        )
    val rsmState2 =
        RSMState(
            id = 2,
            nonterminal = nonterminalS,
        )
    val rsmState3 =
        RSMState(
            id = 3,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState4 =
        RSMState(
            id = 4,
            nonterminal = nonterminalS,
        )
    val rsmState5 =
        RSMState(
            id = 5,
            nonterminal = nonterminalS,
            isFinal = true,
        )
    val rsmState6 =
        RSMState(
            id = 6,
            nonterminal = nonterminalA,
            isStart = true,
        )
    nonterminalA.startState = rsmState6
    val rsmState7 =
        RSMState(
            id = 7,
            nonterminal = nonterminalA,
        )
    val rsmState8 =
        RSMState(
            id = 8,
            nonterminal = nonterminalA,
            isFinal = true,
        )
    val rsmState9 =
        RSMState(
            id = 9,
            nonterminal = nonterminalB,
            isStart = true,
        )
    nonterminalB.startState = rsmState9
    val rsmState10 =
        RSMState(
            id = 10,
            nonterminal = nonterminalB,
            isFinal = true,
        )

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState1,
        ))
    rsmState1.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalB,
            head = rsmState2,
        ))
    rsmState2.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("c"),
            head = rsmState3,
        ))
    rsmState0.addNonterminalEdge(
        RSMNonterminalEdge(
            nonterminal = nonterminalA,
            head = rsmState4,
        ))
    rsmState4.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("c"),
            head = rsmState5,
        ))

    rsmState6.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("a"),
            head = rsmState7,
        ))
    rsmState7.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("b"),
            head = rsmState8,
        ))

    rsmState9.addTerminalEdge(
        RSMTerminalEdge(
            terminal = Terminal("b"),
            head = rsmState10,
        ))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/abc.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'g1' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS)
    val rsmState2 = RSMState(id = 2, nonterminal = nonterminalS)
    val rsmState3 = RSMState(id = 3, nonterminal = nonterminalS, isFinal = true)
    val rsmState4 = RSMState(id = 4, nonterminal = nonterminalS)
    val rsmState5 = RSMState(id = 5, nonterminal = nonterminalS)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState1))
    rsmState1.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState2))
    rsmState2.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState3))

    rsmState1.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState3))

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState4))
    rsmState4.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState5))
    rsmState5.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState3))

    rsmState4.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState3))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/g1.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'g2' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS)
    val rsmState2 = RSMState(id = 2, nonterminal = nonterminalS)
    val rsmState3 = RSMState(id = 3, nonterminal = nonterminalS, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState1))
    rsmState1.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState2))
    rsmState2.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState3))

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState3))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/g2.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'geo' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS)
    val rsmState2 = RSMState(id = 2, nonterminal = nonterminalS)
    val rsmState3 = RSMState(id = 3, nonterminal = nonterminalS, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("broaderTransitive"), head = rsmState1))
    rsmState1.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState2))
    rsmState2.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("broaderTransitive_r"), head = rsmState3))

    rsmState1.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("broaderTransitive_r"), head = rsmState3))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/geo.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'c_analysis' rsm`() {
    val nonterminalS = Nonterminal("S")
    val nonterminalV = Nonterminal("V")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS)
    val rsmState2 = RSMState(id = 2, nonterminal = nonterminalS)
    val rsmState3 = RSMState(id = 3, nonterminal = nonterminalS, isFinal = true)

    nonterminalS.startState = rsmState0

    val rsmState4 = RSMState(id = 4, nonterminal = nonterminalV, isStart = true, isFinal = true)
    val rsmState5 = RSMState(id = 5, nonterminal = nonterminalV)
    val rsmState6 = RSMState(id = 6, nonterminal = nonterminalV, isFinal = true)
    val rsmState7 = RSMState(id = 7, nonterminal = nonterminalV, isFinal = true)
    val rsmState8 = RSMState(id = 8, nonterminal = nonterminalV)
    val rsmState9 = RSMState(id = 9, nonterminal = nonterminalV, isFinal = true)

    nonterminalV.startState = rsmState4

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("d_r"), head = rsmState1))
    rsmState1.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalV, head = rsmState2))
    rsmState2.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("d"), head = rsmState3))

    rsmState4.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a_r"), head = rsmState6))
    rsmState4.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState5))

    rsmState5.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a_r"), head = rsmState6))

    rsmState6.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState8))
    rsmState6.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState9))
    rsmState6.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a_r"), head = rsmState6))
    rsmState6.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState5))
    rsmState6.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState7))

    rsmState7.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState8))
    rsmState7.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState9))

    rsmState8.addNonterminalEdge(RSMNonterminalEdge(nonterminal = nonterminalS, head = rsmState9))

    rsmState9.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState8))
    rsmState9.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("a"), head = rsmState9))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/c_analysis.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'reg1' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState0))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState0))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/reg1.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'reg2' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState0))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState0))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState0))
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState0))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/reg2.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'reg3' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true, isFinal = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS, isStart = true, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState0))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState1))

    rsmState1.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState1))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/reg3.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }

  @Test
  fun `'reg4' rsm`() {
    val nonterminalS = Nonterminal("S")
    val rsmState0 = RSMState(id = 0, nonterminal = nonterminalS, isStart = true, isFinal = true)
    val rsmState1 = RSMState(id = 1, nonterminal = nonterminalS, isStart = true, isFinal = true)
    val rsmState2 = RSMState(id = 2, nonterminal = nonterminalS, isStart = true, isFinal = true)
    val rsmState3 = RSMState(id = 3, nonterminal = nonterminalS, isStart = true, isFinal = true)

    nonterminalS.startState = rsmState0

    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type"), head = rsmState0))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState1))
    rsmState0.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState2))
    rsmState0.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState3))

    rsmState1.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("subClassOf"), head = rsmState1))
    rsmState1.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState2))
    rsmState1.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState3))

    rsmState2.addTerminalEdge(
        RSMTerminalEdge(terminal = Terminal("subClassOf_r"), head = rsmState2))
    rsmState2.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState3))

    rsmState3.addTerminalEdge(RSMTerminalEdge(terminal = Terminal("type_r"), head = rsmState3))

    val pathToTXT = "src/test/resources/cli/TestRSMReadWriteTXT/reg4.txt"
    writeRSMToTXT(rsmState0, pathToTXT)
    val actualRSMState = readRSMFromTXT(pathToTXT)

    assertEquals(expected = rsmState0, actual = actualRSMState)
    assertEquals(
        expected = rsmState0.outgoingTerminalEdges, actual = actualRSMState.outgoingTerminalEdges)
    assertEquals(
        expected = rsmState0.outgoingNonterminalEdges,
        actual = actualRSMState.outgoingNonterminalEdges)
  }
}
