package org.kotgll.cfg.grammar.symbol

interface Terminal : Symbol {
  fun match(pos: Int, input: String): String?
}
