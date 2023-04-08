package org.kotgll.cfg.grammar.symbol

import org.kotgll.cfg.grammar.Alternative

class Optional(symbol: Symbol) : Regular(symbol, "?") {
  init {
    addAlternative(Alternative(listOf(symbol)))
    addAlternative(Alternative(emptyList()))
  }
}
