package org.kotgll.rsm.stringinput

import org.kotgll.rsm.grammar.RSMState
import org.kotgll.rsm.stringinput.sppf.SPPFNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue(size: Int) {
    val todo: ArrayDeque<Descriptor> = ArrayDeque()
    val done: Array<HashSet<Descriptor>> = Array(size) { HashSet() }

    fun add(rsmState: RSMState, gssNode: GSSNode, pos: Int, sppfNode: SPPFNode?) {
        val descriptor = Descriptor(rsmState, gssNode, sppfNode, pos)
        if (!done[pos].contains(descriptor)) {
            done[pos].add(descriptor)
            todo.add(descriptor)
        }
    }

    fun next() = todo.removeFirst()

    fun isEmpty() = todo.isEmpty()

    class Descriptor(val rsmState: RSMState, val gssNode: GSSNode, val sppfNode: SPPFNode?, val pos: Int) {
        override fun toString() = "Descriptor(rsmState=$rsmState, gssNode=$gssNode, sppfNode=$sppfNode, pos=$pos)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Descriptor) return false

            if (rsmState != other.rsmState) return false
            if (gssNode != other.gssNode) return false
            if (sppfNode != other.sppfNode) return false
            if (pos != other.pos) return false

            return true
        }

        override fun hashCode() = Objects.hash(rsmState, gssNode, sppfNode, pos)
    }

}