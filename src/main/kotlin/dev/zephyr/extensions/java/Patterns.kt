package dev.zephyr.extensions.java

import dev.zephyr.extensions.repeatWhile
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.replaceBy(pattern: Pattern, replacer: (Matcher) -> String) = buildString {
    val buffer = StringBuffer()
    val matcher = pattern.matcher(this@replaceBy)

    matcher.repeatWhile({ it.find() }) {
        it.appendReplacement(this, replacer(it))
    }

    matcher.appendTail(this)
}