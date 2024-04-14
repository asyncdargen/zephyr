package dev.zephyr.util.kotlin

import java.nio.file.FileVisitResult
import java.nio.file.Path
import kotlin.io.path.visitFileTree

fun Path.listFilesRecursive(relativize: Boolean = false) = buildList {
    @OptIn(kotlin.io.path.ExperimentalPathApi::class)
    visitFileTree {
        onVisitFile { file, _ ->
            add(if (relativize) this@listFilesRecursive.relativize(file) else file)
            FileVisitResult.CONTINUE
        }
    }
}