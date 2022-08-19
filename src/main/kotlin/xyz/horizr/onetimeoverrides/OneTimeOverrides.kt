package xyz.horizr.onetimeoverrides

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.FileAlreadyExistsException
import java.nio.file.LinkOption
import java.nio.file.Path
import kotlin.io.path.*

val fabricLoader: FabricLoader = FabricLoader.getInstance()

fun Path.getPathsRecursively(relativeTo: Path = this): Set<Path> {
    return buildSet {
        for (path in listDirectoryEntries()) {
            if (path.isRegularFile(LinkOption.NOFOLLOW_LINKS)) add(path.relativeTo(relativeTo))
            else if (path.isDirectory(LinkOption.NOFOLLOW_LINKS)) addAll(path.getPathsRecursively(relativeTo))
        }
    }
}

const val OVERRIDES_DIRECTORY_NAME = "one-time-overrides"

@Suppress("unused")
fun init() {
    val logger = KotlinLogging.logger("OneTimeOverrides")

    val oneTimeOverridesDirectory = fabricLoader.gameDir.resolve(OVERRIDES_DIRECTORY_NAME)
    if (!oneTimeOverridesDirectory.isDirectory()) {
        logger.warn("$OVERRIDES_DIRECTORY_NAME directory does not exist.")
        return
    }

    val allOverrides = oneTimeOverridesDirectory.getPathsRecursively()
    if (allOverrides.isEmpty()) {
        logger.warn("$OVERRIDES_DIRECTORY_NAME directory does not contain any overrides.")
        return
    }

    fun copyOverrideFile(relativePath: Path): Boolean = try {
        val outputPath = fabricLoader.gameDir.resolve(relativePath)
        outputPath.parent.createDirectories()
        oneTimeOverridesDirectory.resolve(relativePath).copyTo(outputPath, false)
        true
    } catch (e: FileAlreadyExistsException) {
        false
    }

    var newCount by atomic(0)

    runBlocking(Dispatchers.IO) {
        for (relativePath in allOverrides) {
            launch {
                val wasNew = copyOverrideFile(relativePath)

                if (wasNew) {
                    logger.debug("Copied new override: $relativePath")
                    newCount++
                }
            }
        }
    }

    logger.info("Copied $newCount new override(s).")
}
