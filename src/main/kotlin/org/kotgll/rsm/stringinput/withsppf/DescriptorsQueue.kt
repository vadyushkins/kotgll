package org.kotgll.rsm.stringinput.withsppf

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.stringinput.withsppf.sppf.SPPFNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue(size: Int) {
  val todo: ArrayDeque<Descriptor> = ArrayDeque()
  val created: Array<HashSet<Descriptor>> = Array(size) { HashSet() }

  fun add(rsmState: RSMState, gssNode: GSSNode, sppfNode: SPPFNode?, pos: Int) {
    val descriptor = Descriptor(rsmState, gssNode, sppfNode, pos)
    if (!created[pos].contains(descriptor)) {
      created[pos].add(descriptor)
      todo.addLast(descriptor)
    }
  }

  fun next() = todo.removeFirst()

  fun isEmpty() = todo.isEmpty()

  class Descriptor(
      val rsmState: RSMState,
      val gssNode: GSSNode,
      val sppfNode: SPPFNode?,
      val pos: Int,
  ) {
    override fun toString() =
        "Descriptor(rsmState=$rsmState, gssNode=$gssNode, sppfNode=$sppfNode, pos=$pos)"

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Descriptor) return false

      if (rsmState != other.rsmState) return false
      if (gssNode != other.gssNode) return false
      if (sppfNode != other.sppfNode) return false
      if (pos != other.pos) return false

      return true
    }

    val hashCode: Int = Objects.hash(rsmState, gssNode, sppfNode)
    override fun hashCode() = hashCode
  }
}
