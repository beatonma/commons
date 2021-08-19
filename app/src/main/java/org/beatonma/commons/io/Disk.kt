package org.beatonma.commons.io

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

suspend fun Context.copyFile(sourcePath: String, targetPath: String): Long =
    withContext(Dispatchers.IO) {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) {
            throw FileNotFoundException("source path='$sourcePath' [target path='$targetPath']")
        }

        val inputStream = assets.open(sourcePath)
        val outputStream = FileOutputStream(targetPath)

        val bytesCopied = inputStream.copyTo(outputStream, bufferSize = 8 * 1024)
        inputStream.close()

        outputStream.apply {
            flush()
            close()
        }

        bytesCopied
    }
