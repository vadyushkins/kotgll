package org.kotgll

import org.kotgll.sppf.SPPFNode

interface Parser {
    fun parse(pos: Int, cu: GSSNode, cn: SPPFNode?, ctx: GLL)
}