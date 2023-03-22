package org.kotgll.symbol

import org.kotgll.Alternative

class Optional(symbol: Symbol) : Regular(symbol, "?") {
    init {
        addAlternative(Alternative(listOf(symbol)))
        addAlternative(Alternative(emptyList()))
    }
}