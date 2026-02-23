package dev.igorcferreira.cloudkitfeatureflag.di

import android.os.Environment
import okio.Path.Companion.toOkioPath

actual fun fetchRootFilePath(): String {
    //TODO: Investigate usage of Context to provide file root.
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        .toOkioPath()
        .toString()
}
