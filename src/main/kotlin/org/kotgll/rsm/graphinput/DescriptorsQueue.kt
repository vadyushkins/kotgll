package org.kotgll.rsm.graphinput

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.graphinput.graph.GraphNode
import org.kotgll.rsm.graphinput.sppf.SPPFNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue {
  val todo: ArrayDeque<Descriptor> = ArrayDeque()
  val done: HashMap<Int, HashSet<Int>> = HashMap()

  fun add(rsmState: RSMState, gssNode: GSSNode, pos: GraphNode, sppfNode: SPPFNode?) {
    val descriptor = Descriptor(rsmState, gssNode, sppfNode, pos)
    if (!done.containsKey(pos.hashCode)) done[pos.hashCode] = HashSet()
    if (!done[pos.hashCode]!!.contains(descriptor.hashCode)) {
      done[pos.hashCode]!!.add(descriptor.hashCode)
      todo.add(descriptor)
    }
  }

  fun next() = todo.removeFirst()

  fun isEmpty() = todo.isEmpty()

  class Descriptor(
      val rsmState: RSMState,
      val gssNode: GSSNode,
      val sppfNode: SPPFNode?,
      val pos: GraphNode,
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

    val hashCode: Int = Objects.hash(rsmState, gssNode, sppfNode, pos)
    override fun hashCode() = hashCode
  }
}
