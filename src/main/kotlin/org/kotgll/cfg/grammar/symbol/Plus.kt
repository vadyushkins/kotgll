package org.kotgll.cfg.grammar.symbol

import org.kotgll.cfg.grammar.Alternative

class Plus(symbol: Symbol) : Regular(symbol, "+") {
  init {
    addAlternative(Alternative(ArrayList(listOf(symbol))))
    addAlternative(Alternative(ArrayList(listOf(symbol, this))))
  }
}
