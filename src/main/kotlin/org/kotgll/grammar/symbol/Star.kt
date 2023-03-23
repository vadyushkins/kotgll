package org.kotgll.grammar.symbol

import org.kotgll.grammar.Alternative

class Star(symbol: Symbol) : Regular(symbol, "*") {
    init {
        addAlternative(Alternative(listOf()))
        addAlternative(Alternative(listOf(symbol, this)))
    }
}