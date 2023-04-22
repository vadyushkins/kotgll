package org.kotgll.cfg.graphinput.withoutsppf

import org.kotgll.cfg.grammar.Alternative
import org.kotgll.graph.GraphNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue {
  val todo: ArrayDeque<Descriptor> = ArrayDeque()
  val created: HashMap<GraphNode, HashSet<Descriptor>> = HashMap()

  fun add(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: GraphNode) {
    val descriptor = Descriptor(alternative, dot, gssNode, pos)
    if (!created.containsKey(pos)) created[pos] = HashSet()
    if (created[pos]!!.add(descriptor)) todo.addLast(descriptor)
  }

  fun next() = todo.removeFirst()

  fun isEmpty() = todo.isEmpty()

  class Descriptor(
      val alternative: Alternative,
      val dot: Int,
      val gssNode: GSSNode,
      val pos: GraphNode,
  ) {
    override fun toString() =
        "Descriptor(alternative=$alternative, dot=$dot, gssNode=$gssNode, pos=$pos)"

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Descriptor) return false

      if (alternative != other.alternative) return false
      if (dot != other.dot) return false
      if (gssNode != other.gssNode) return false

      return true
    }

    val hashCode: Int = Objects.hash(alternative, dot, gssNode)
    override fun hashCode() = hashCode
  }
}
