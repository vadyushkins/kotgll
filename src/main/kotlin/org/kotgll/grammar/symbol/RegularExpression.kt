package org.kotgll.grammar.symbol

import java.util.regex.Matcher
import java.util.regex.Pattern

class RegularExpression(regex: String) : Terminal {
    val pattern: Pattern = Pattern.compile(regex)

    override fun match(pos: Int, input: String): String? {
        val matcher: Matcher = pattern.matcher(input.subSequence(pos, input.length))
        return if (matcher.lookingAt()) matcher.group() else null
    }

    override fun toString() = pattern.toString()
}