package org.kotgll.rsm.grammar

import java.io.File

fun writeRSMToTXT(rsm: RSMState, pathToTXT: String) {
  val states: MutableList<RSMState> = mutableListOf()
  val edges: HashMap<RSMState, MutableList<RSMEdge>> = hashMapOf()

  val queue: ArrayDeque<RSMState> = ArrayDeque(listOf(rsm))
  while (!queue.isEmpty()) {
    val v = queue.removeFirst()
    states.add(v)
    if (!edges.containsKey(v)) edges[v] = mutableListOf()
    for (edge in v.outgoingTerminalEdges) {
      if (!edges[v]!!.contains(edge)) edges[v]!!.add(edge)
      if (!states.contains(edge.head)) queue.addLast(edge.head)
    }
    for (edge in v.outgoingNonterminalEdges) {
      if (!edges[v]!!.contains(edge)) edges[v]!!.add(edge)
      if (!states.contains(edge.head)) queue.addLast(edge.head)
      if (!states.contains(edge.nonterminal.startState)) queue.addLast(edge.nonterminal.startState)
    }
  }

  File(pathToTXT).printWriter().use { out ->
    out.println(
        """StartState(
      |id=${rsm.id},
      |nonterminal=Nonterminal("${rsm.nonterminal.name}"),
      |isStart=${rsm.isStart},
      |isFinal=${rsm.isFinal}
      |)"""
            .trimMargin()
            .replace("\n", ""))
    states.forEach { state ->
      out.println(
          """State(
      |id=${state.id},
      |nonterminal=Nonterminal("${state.nonterminal.name}"),
      |isStart=${state.isStart},
      |isFinal=${state.isFinal}
      |)"""
              .trimMargin()
              .replace("\n", ""))
    }
    states.forEach { state ->
      edges[state]?.forEach { edge ->
        if (edge is RSMTerminalEdge) {
          out.println(
              """TerminalEdge(
      |tail=${state.id},
      |head=${edge.head.id},
      |terminal=Terminal("${edge.terminal.value}")
      |)"""
                  .trimMargin()
                  .replace("\n", ""))
        } else if (edge is RSMNonterminalEdge) {
          out.println(
              """NonterminalEdge(
      |tail=${state.id},
      |head=${edge.head.id},
      |nonterminal=Nonterminal("${edge.nonterminal.name}")
      |)"""
                  .trimMargin()
                  .replace("\n", ""))
        }
      }
    }
  }
}
