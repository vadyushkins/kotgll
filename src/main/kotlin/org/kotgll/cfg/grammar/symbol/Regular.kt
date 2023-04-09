package org.kotgll.cfg.grammar.symbol

import java.util.*

open class Regular(
    val symbol: Symbol,
    val suffix: String,
) : Nonterminal(symbol.toString() + suffix) {
  override fun toString() = "${this.javaClass.name}($symbol$suffix)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Regular) return false
    if (!super.equals(other)) return false

    if (symbol != other.symbol) return false
    if (suffix != other.suffix) return false

    return true
  }

  override val hashCode: Int = Objects.hash(symbol, suffix)
  override fun hashCode() = hashCode
}
