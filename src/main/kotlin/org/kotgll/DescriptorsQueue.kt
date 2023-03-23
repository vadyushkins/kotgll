package org.kotgll

import org.kotgll.sppf.SPPFNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue(size: Int) {
    val todo: ArrayDeque<Descriptor> = ArrayDeque()
    val done: Array<HashSet<Descriptor>> = Array(size) { HashSet() }

    fun add(alternative: Alternative, dot: Int, gssNode: GSSNode, pos: Int, sppfNode: SPPFNode?) {
        val descriptor = Descriptor(alternative, dot, gssNode, sppfNode, pos)
        if (!done[pos].contains(descriptor)) {
            done[pos].add(descriptor)
            todo.add(descriptor)
        }
    }

    fun next() = todo.removeFirst()

    fun isEmpty() = todo.isEmpty()

    class Descriptor(
        val alternative: Alternative,
        val dot: Int,
        val gssNode: GSSNode,
        val sppfNode: SPPFNode?,
        val pos: Int,
    ) {
        override fun toString() = "Descriptor(" +
                "alternative=$alternative, " +
                "dot=$dot, " +
                "gssNode=$gssNode, " +
                "sppfNode=$sppfNode, " +
                "pos=$pos)"

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

        override fun hashCode() = Objects.hash(alternative, dot, gssNode, sppfNode, pos)

        fun parse(driver: GLL) {
            if (dot == 0) {
                alternative.parse(pos, gssNode, sppfNode, driver)
            } else {
                alternative.parseAt(dot, pos, gssNode, sppfNode, driver)
            }
        }
    }

}