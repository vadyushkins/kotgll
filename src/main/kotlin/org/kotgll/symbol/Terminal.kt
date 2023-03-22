package org.kotgll.symbol

import org.kotgll.GLL

interface Terminal : Symbol {
    fun match(pos: Int, driver: GLL): String?
}