package dev.zephyr.util.java

import dev.zephyr.util.kotlin.repeatWhile
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.replaceBy(pattern: Pattern, replacer: (Matcher) -> String) = buildString {
    val matcher = pattern.matcher(this@replaceBy)

    matcher.repeatWhile({ it.find() }) {
        it.appendReplacement(this, replacer(it))
    }

    matcher.appendTail(this)
}