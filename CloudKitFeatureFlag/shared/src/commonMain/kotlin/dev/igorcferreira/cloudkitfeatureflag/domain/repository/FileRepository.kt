package dev.igorcferreira.cloudkitfeatureflag.domain.repository

import okio.FileSystem
import okio.Path.Companion.toPath

internal class FileRepository(
    private val fileSystem: FileSystem,
    private val rootPath: String
) {
    fun readFile(named: String) = read(pathForFile(named))
    fun writeFile(named: String, content: String) = write(content, pathForFile(named))

    private fun pathForFile(file: String) = "$rootPath/$file".toPath()
    private fun read(path: okio.Path) = if (fileSystem.exists(path)) {
        fileSystem.read(path) {
            readUtf8()
        }
    } else {
        null
    }
    private fun write(file: String, path: okio.Path) {
        if (fileSystem.exists(path)) {
            fileSystem.delete(path)
        }
        fileSystem.write(path) {
            writeUtf8(file)
        }
    }
}
