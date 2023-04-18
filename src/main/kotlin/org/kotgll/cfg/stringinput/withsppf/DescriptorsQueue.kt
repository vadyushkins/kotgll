package org.kotgll.cfg.stringinput.withsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.cfg.stringinput.withsppf.sppf.SPPFNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue(size: Int) {
  val todo: ArrayDeque<Descriptor> = ArrayDeque()
  val created: Array<HashSet<Descriptor>> = Array(size) { HashSet() }

  fun add(alternative: Alternative, dot: Int, gssNode: GSSNode, sppfNode: SPPFNode?, pos: Int) {
    val descriptor = Descriptor(alternative, dot, gssNode, sppfNode, pos)
    if (!created[pos].contains(descriptor)) {
      created[pos].add(descriptor)
      todo.addLast(descriptor)
    }
  }

  fun next() = todo.removeFirst()

  fun isEmpty() = todo.isEmpty()

  class Descriptor(
      val alternative: Alternative,
      val dot: Int,
      val gssNode: GSSNode,
      val sppfNode: SPPFNode?,
      val pos: Int
  ) {
    override fun toString() =
        "Descriptor(alternative=$alternative, dot=$dot, gssNode=$gssNode, sppfNode=$sppfNode, pos=$pos)"

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Descriptor) return false

      if (alternative != other.alternative) return false
      if (dot != other.dot) return false
      if (gssNode != other.gssNode) return false
      if (sppfNode != other.sppfNode) return false
      if (pos != other.pos) return false

      return true
    }

    val hashCode: Int = Objects.hash(alternative, dot, gssNode, sppfNode)
    override fun hashCode() = hashCode
  }
}
