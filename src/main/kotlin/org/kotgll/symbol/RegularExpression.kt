package org.kotgll.symbol

import org.kotgll.GLL
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegularExpression(val regex: String) : Terminal {
    val pattern: Pattern = Pattern.compile(regex)

    override fun match(pos: Int, driver: GLL): String? {
        val matcher: Matcher = pattern.matcher(
            driver.input.subSequence(pos, driver.input.length)
        )
        if (matcher.lookingAt()) {
            return matcher.group()
        }
        return null
    }

    override fun toString() = pattern.toString()
}