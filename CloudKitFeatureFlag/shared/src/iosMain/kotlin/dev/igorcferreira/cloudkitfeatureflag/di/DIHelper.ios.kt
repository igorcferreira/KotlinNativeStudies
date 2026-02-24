package dev.igorcferreira.cloudkitfeatureflag.di

import dev.igorcferreira.cloudkitfeatureflag.domain.repository.FileRepository
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val fileModule: Module = module {
    factory<FileRepository> {
        val url = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val rootPath = url?.path() ?: "."
        return@factory FileRepository(
            fileSystem = FileSystem.SYSTEM,
            rootPath = rootPath,
        )
    }
}
