package org.kotgll.cfg.grammar.symbol

import org.kotgll.cfg.grammar.Alternative

class Star(symbol: Symbol) : Regular(symbol, "*") {
  init {
    addAlternative(Alternative(ArrayList()))
    addAlternative(Alternative(ArrayList(listOf(symbol, this))))
  }
}
