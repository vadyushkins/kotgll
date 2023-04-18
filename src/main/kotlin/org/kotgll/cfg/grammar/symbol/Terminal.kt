package org.kotgll.cfg.grammar.symbol

class Terminal(val value: String) : Symbol {
  val size: Int = value.length
  fun match(pos: Int, input: String) = input.startsWith(value, pos)

  override fun toString() = "Terminal($value)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Terminal) return false

    if (value != other.value) return false

    return true
  }

  val hashCode: Int = value.hashCode()
  override fun hashCode() = hashCode
}
