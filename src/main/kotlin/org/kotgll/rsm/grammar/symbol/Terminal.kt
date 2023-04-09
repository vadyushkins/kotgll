package org.kotgll.rsm.grammar.symbol

interface Terminal : Symbol {
  fun match(pos: Int, input: String): String?
}
