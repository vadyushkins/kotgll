package org.kotgll

import org.kotgll.sppf.SPPFNode
import java.util.*
import kotlin.collections.ArrayDeque

class DescriptorsQueue(size: Int) {
    val todo: ArrayDeque<Descriptor> = ArrayDeque()
    val done: Array<HashSet<Configuration>> = Array(size) { HashSet() }

    fun add(parser: Parser, gssNode: GSSNode, pos: Int, sppfNode: SPPFNode?) {
        val config = Configuration(parser, gssNode, sppfNode)
        if (!done[pos].contains(config)) {
            done[pos].add(config)
            todo.add(Descriptor(config, pos))
        }
    }

    fun next() = todo.removeFirst()

    fun isEmpty() = todo.isEmpty()

    class Configuration(
        val parser: Parser,
        val gssNode: GSSNode,
        val sppfNode: SPPFNode?,
    ) {
        override fun toString() = "Configuration(" +
                "parser=$parser, " +
                "gssNode=$gssNode, " +
                "sppfNode=$sppfNode)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Configuration) return false

            if (parser != other.parser) return false
            if (gssNode != other.gssNode) return false
            if (sppfNode != other.sppfNode) return false

            return true
        }

        override fun hashCode() = Objects.hash(parser, gssNode, sppfNode)

        fun parse(pos: Int, driver: GLL) {
            parser.parse(pos, gssNode, sppfNode, driver)
        }
    }

    class Descriptor(val config: Configuration, val pos: Int) {
        override fun toString() = "Descriptor(config=$config, pos=$pos)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Descriptor) return false

            if (config != other.config) return false
            if (pos != other.pos) return false

            return true
        }

        override fun hashCode() = Objects.hash(config, pos)

        fun parse(driver: GLL) {
            config.parse(pos, driver)
        }
    }

}