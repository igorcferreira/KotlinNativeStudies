package dev.igorcferreira.cloudkitfeatureflag.di

import dev.igorcferreira.cloudkitfeatureflag.domain.repository.FileRepository
import okio.FileSystem
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val fileModule: Module = module {
    factory<FileRepository> {
        val context = androidContext()
        return@factory FileRepository(
            fileSystem = FileSystem.SYSTEM,
            rootPath = context.filesDir.path,
        )
    }
}
