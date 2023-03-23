package org.kotgll

import org.kotgll.sppf.SPPFNode
import org.kotgll.symbol.Nonterminal
import org.kotgll.symbol.Symbol
import org.kotgll.symbol.Terminal
import java.util.*

class Alternative(val elements: List<Symbol>) : Parser {
    val items: MutableList<Item> = ArrayList()
    lateinit var nonterminal: Nonterminal

    init {
        for (i in 0..elements.size) {
            items.add(Item(this, i))
        }
    }

    override fun parse(pos: Int, cu: GSSNode, cn: SPPFNode?, ctx: GLL) {
        if (elements.isEmpty()) {
            val cr: SPPFNode = ctx.getNodeE(pos)
            val ncn: SPPFNode? = ctx.getNodeP(this.items[0], cn, cr)
            ctx.pop(cu, ncn, pos)
        } else {
            parseAt(0, pos, cu, cn, ctx)
        }
    }

    fun parseAt(dot: Int, pos: Int, cu: GSSNode, cn: SPPFNode?, ctx: GLL) {
        var curPos: Int = pos
        var curGSSNode: GSSNode = cu
        var curSPPFNode: SPPFNode? = cn
        for (i in dot until elements.size) {
            val curSymbol: Symbol = elements[i]

            if (curSymbol is Terminal) {
                if (ctx.isAtEnd(curPos)) return
                val value: String? = curSymbol.match(curPos, ctx)
                if (value != null) {
                    val skip: Int = value.length
                    val cr: SPPFNode = ctx.getNodeT(curSymbol, value, curPos, skip)
                    curSPPFNode = ctx.getNodeP(items[i + 1], curSPPFNode, cr)
                    curPos += skip
                    continue
                }
                return
            }

            if (curSymbol is Nonterminal) {
                curGSSNode = ctx.createGSSNode(items[i + 1], curGSSNode, curSPPFNode, curPos)
                for (alt in curSymbol) {
                    ctx.add(alt, curGSSNode, curPos, null)
                }
                return
            }
        }
        ctx.pop(curGSSNode, curSPPFNode, pos)
    }

    override fun toString() = "Alternative(elements=$elements)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Alternative) return false

        if (elements != other.elements) return false
        if (items != other.items) return false
        if (nonterminal != other.nonterminal) return false

        return true
    }

    override fun hashCode() = Objects.hash(elements)
}