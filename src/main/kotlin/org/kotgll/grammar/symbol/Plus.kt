package org.kotgll.grammar.symbol

import org.kotgll.grammar.Alternative

class Plus(symbol: Symbol) : Regular(symbol, "+") {
    init {
        addAlternative(Alternative(listOf(symbol)))
        addAlternative(Alternative(listOf(symbol, this)))
    }
}