package org.kotgll.rsm.grammar.symbol

import kotlin.Char

class Char(val char: Char) : Terminal {
  override fun match(pos: Int, input: String): String? = if (input[pos] == char) char + "" else null

  override fun toString() = "Char('$char')"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is org.kotgll.rsm.grammar.symbol.Char) return false

    if (char != other.char) return false

    return true
  }

  val hashCode: Int = char.hashCode()
  override fun hashCode() = hashCode
}
