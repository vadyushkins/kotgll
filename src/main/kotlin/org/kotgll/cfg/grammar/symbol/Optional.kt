package org.kotgll.cfg.grammar.symbol

import org.kotgll.cfg.grammar.Alternative

class Optional(symbol: Symbol) : Regular(symbol, "?") {
  init {
    addAlternative(Alternative(ArrayList(listOf(symbol))))
    addAlternative(Alternative(ArrayList()))
  }
}
