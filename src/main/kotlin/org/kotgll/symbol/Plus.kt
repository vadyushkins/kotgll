package org.kotgll.symbol

import org.kotgll.Alternative

class Plus(symbol: Symbol) : Regular(symbol, "+") {
    init {
        addAlternative(Alternative(listOf(symbol)))
        addAlternative(Alternative(listOf(symbol, this)))
    }

    fun isBase(alternative: Alternative) = alternative == this.alternatives[0]

    fun isRecursive(alternative: Alternative) = alternative == this.alternatives[1]
}