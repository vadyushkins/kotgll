package org.kotgll.rsm.grammar

import org.kotgll.rsm.grammar.symbol.Nonterminal
import org.kotgll.rsm.grammar.symbol.Terminal
import java.io.File

fun readRSMFromTXT(pathToTXT: String): RSMState {
  var startRSMState: RSMState? = null
  val rsmStates: HashMap<Int, RSMState> = HashMap()
  fun makeRSMState(
      id: Int,
      nonterminal: Nonterminal,
      isStart: Boolean = false,
      isFinal: Boolean = false
  ): RSMState {
    val y = RSMState(id, nonterminal, isStart, isFinal)
    if (!rsmStates.containsKey(y.hashCode)) rsmStates[y.hashCode] = y
    return rsmStates[y.hashCode]!!
  }

  val nonterminals: HashMap<Nonterminal, Nonterminal> = HashMap()
  fun makeNonterminal(name: String): Nonterminal {
    val y = Nonterminal(name)
    if (!nonterminals.contains(y)) nonterminals[y] = y
    return nonterminals[y]!!
  }

  val startStateRegex =
      """^StartState\(
    |id=(?<id>.*),
    |nonterminal=Nonterminal\("(?<nonterminalValue>.*)"\),
    |isStart=(?<isStart>.*),
    |isFinal=(?<isFinal>.*)
    |\)$"""
          .trimMargin()
          .replace("\n", "")
          .toRegex()
  val rsmStateRegex =
      """^State\(
    |id=(?<id>.*),
    |nonterminal=Nonterminal\("(?<nonterminalValue>.*)"\),
    |isStart=(?<isStart>.*),
    |isFinal=(?<isFinal>.*)
    |\)$"""
          .trimMargin()
          .replace("\n", "")
          .toRegex()
  val rsmTerminalEdgeRegex =
      """^TerminalEdge\(
        |tail=(?<tailId>.*),
        |head=(?<headId>.*),
        |terminal=Terminal\("(?<literalValue>.*)"\)
        |\)$"""
          .trimMargin()
          .replace("\n", "")
          .toRegex()
  val rsmNonterminalEdgeRegex =
      """^NonterminalEdge\(
        |tail=(?<tailId>.*),
        |head=(?<headId>.*),
        |nonterminal=Nonterminal\("(?<nonterminalValue>.*)"\)
        |\)$"""
          .trimMargin()
          .replace("\n", "")
          .toRegex()

  val reader = File(pathToTXT).inputStream().bufferedReader()
  while (true) {
    val line = reader.readLine() ?: break

    if (startStateRegex.matches(line)) {
      val (idValue, nonterminalValue, isStartValue, isFinalValue) =
          startStateRegex.matchEntire(line)!!.destructured

      val tmpNonterminal = makeNonterminal(nonterminalValue)
      startRSMState =
          makeRSMState(
              id = idValue.toInt(),
              nonterminal = tmpNonterminal,
              isStart = isStartValue == "true",
              isFinal = isFinalValue == "true",
          )
      if (startRSMState.isStart) tmpNonterminal.startState = startRSMState
    } else if (rsmStateRegex.matches(line)) {
      val (idValue, nonterminalValue, isStartValue, isFinalValue) =
          rsmStateRegex.matchEntire(line)!!.destructured
      val tmpNonterminal = makeNonterminal(nonterminalValue)
      val tmpRSMState =
          makeRSMState(
              id = idValue.toInt(),
              nonterminal = tmpNonterminal,
              isStart = isStartValue == "true",
              isFinal = isFinalValue == "true",
          )
      if (tmpRSMState.isStart) tmpNonterminal.startState = tmpRSMState
    } else if (rsmTerminalEdgeRegex.matches(line)) {
      val (tailId, headId, terminalValue) = rsmTerminalEdgeRegex.matchEntire(line)!!.destructured
      val tailRSMState = rsmStates[tailId.toInt()]!!
      val headRSMState = rsmStates[headId.toInt()]!!
      tailRSMState.addTerminalEdge(
          RSMTerminalEdge(terminal = Terminal(terminalValue), head = headRSMState))
    } else if (rsmNonterminalEdgeRegex.matches(line)) {
      val (tailId, headId, nonterminalValue) =
          rsmNonterminalEdgeRegex.matchEntire(line)!!.destructured
      val tailRSMState = rsmStates[tailId.toInt()]!!
      val headRSMState = rsmStates[headId.toInt()]!!
      tailRSMState.addNonterminalEdge(
          RSMNonterminalEdge(nonterminal = makeNonterminal(nonterminalValue), head = headRSMState))
    }
  }

  return startRSMState!!
}
