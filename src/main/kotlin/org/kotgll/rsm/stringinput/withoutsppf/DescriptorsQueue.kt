package org.kotgll.rsm.stringinput.withoutsppf

import org.kotgll.rsm.grammar.RSMState
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue(size: Int) {
  val todo: ArrayDeque<Descriptor> = ArrayDeque()
  val done: Array<HashMap<Int, Descriptor>> = Array(size) { HashMap() }

  fun add(rsmState: RSMState, gssNode: GSSNode, pos: Int) {
    val descriptor = Descriptor(rsmState, gssNode, pos)
    if (!done[pos].containsKey(descriptor.hashCode)) {
      done[pos][descriptor.hashCode] = descriptor
      todo.add(descriptor)
    }
  }

  fun next() = todo.removeFirst()

  fun isEmpty() = todo.isEmpty()

  class Descriptor(val rsmState: RSMState, val gssNode: GSSNode, val pos: Int) {
    override fun toString() = "Descriptor(rsmState=$rsmState, gssNode=$gssNode, pos=$pos)"

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Descriptor) return false

      if (rsmState != other.rsmState) return false
      if (gssNode != other.gssNode) return false
      if (pos != other.pos) return false

      return true
    }

    val hashCode: Int = Objects.hash(rsmState, gssNode, pos)
    override fun hashCode() = hashCode
  }
}
