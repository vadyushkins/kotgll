package org.kotgll.grammar.symbol

import org.kotgll.grammar.Alternative

class Optional(symbol: Symbol) : Regular(symbol, "?") {
    init {
        addAlternative(Alternative(listOf(symbol)))
        addAlternative(Alternative(emptyList()))
    }
}