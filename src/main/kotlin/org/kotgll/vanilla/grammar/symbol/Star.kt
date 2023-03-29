package org.kotgll.vanilla.grammar.symbol

import org.kotgll.vanilla.grammar.Alternative

class Star(symbol: Symbol) : Regular(symbol, "*") {
    init {
        addAlternative(Alternative(listOf()))
        addAlternative(Alternative(listOf(symbol, this)))
    }
}