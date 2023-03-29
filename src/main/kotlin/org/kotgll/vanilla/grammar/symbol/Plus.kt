package org.kotgll.vanilla.grammar.symbol

import org.kotgll.vanilla.grammar.Alternative

class Plus(symbol: Symbol) : Regular(symbol, "+") {
    init {
        addAlternative(Alternative(listOf(symbol)))
        addAlternative(Alternative(listOf(symbol, this)))
    }
}