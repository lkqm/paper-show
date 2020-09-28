package com.github.lkqm.paper.util

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.utils.IOUtils
import org.apache.commons.lang3.StringUtils
import java.io.*
import java.util.*

/**
 * 压缩相关工具类
 */
object ArchiveUtilsExt {
    const val BUFFER_SIZE = 1024

    /**
     * 解压 zip 文件
     *
     * @param zipFile zip 压缩文件
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名的 list
     * @throws Exception
     */
    fun unZip(zipFile: File, destDir: String): List<String> {
        // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
        var destDir = destDir
        if (StringUtils.isBlank(destDir)) {
            destDir = zipFile.parent
        }
        destDir = if (destDir.endsWith(File.separator)) destDir else destDir + File.separator
        val destDirFile = File(destDir)
        if (!destDirFile.exists()) {
            destDirFile.mkdirs()
        }
        val zipPrefixPath = getZipPrefixPath(zipFile)
        var `is`: ZipArchiveInputStream? = null
        val fileNames: MutableList<String> = ArrayList()
        try {
            `is` = ZipArchiveInputStream(BufferedInputStream(FileInputStream(zipFile), BUFFER_SIZE))
            var entry: ZipArchiveEntry? = null
            while (`is`.nextZipEntry.also { entry = it } != null) {
                fileNames.add(entry!!.name)
                var contentFileName = entry!!.name
                if (zipPrefixPath != null && contentFileName.startsWith(zipPrefixPath)) {
                    contentFileName = contentFileName.substring(zipPrefixPath.length)
                }
                if (entry!!.isDirectory) {
                    val directory = File(destDir, contentFileName)
                    directory.mkdirs()
                } else {
                    val targetFile = File(destDir, contentFileName)
                    val targetDir = targetFile.parentFile
                    targetDir.mkdirs()
                    var os: OutputStream? = null
                    try {
                        os = BufferedOutputStream(FileOutputStream(targetFile), BUFFER_SIZE)
                        IOUtils.copy(`is`, os)
                    } finally {
                        IOUtils.closeQuietly(os)
                    }
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            IOUtils.closeQuietly(`is`)
        }
        return fileNames
    }

    /**
     * 解压 zip 文件
     *
     * @param zipfile zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名的 list
     * @throws Exception
     */
    fun unZip(zipfile: String?, destDir: String): List<String> {
        val zipFile = File(zipfile)
        return unZip(zipFile, destDir)
    }

    /**
     * 获取统一前缀
     *
     * @param zipFile
     * @return
     */
    private fun getZipPrefixPath(zipFile: File): String? {
        val paths = getZipContentFilePaths(zipFile)
        var prefix: String? = null
        for (i in paths.indices) {
            val path = paths[i]
            var hasPrefix = false
            if (path.startsWith("__MACOSX") || path.startsWith(".DS_Store")) continue
            for (j in i + 1 until paths.size) {
                val path2 = paths[j]
                if (path2.startsWith("__MACOSX") || path2.startsWith(".DS_Store")) continue
                if (!path2.startsWith(path)) {
                    break
                }
                if (j == paths.size - 1) {
                    hasPrefix = true
                }
            }
            if (hasPrefix) {
                prefix = path
                break
            }
        }
        return prefix
    }

    /**
     * 获取指定zip压缩内容文件路径
     *
     * @param zipFile
     * @return
     */
    private fun getZipContentFilePaths(zipFile: File): List<String> {
        val fileNames: MutableList<String> = ArrayList()
        var `is`: ZipArchiveInputStream? = null
        try {
            `is` = ZipArchiveInputStream(BufferedInputStream(FileInputStream(zipFile)))
            var entry: ZipArchiveEntry? = null
            while (`is`.nextZipEntry.also { entry = it } != null) {
                fileNames.add(entry!!.name)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            IOUtils.closeQuietly(`is`)
        }
        return fileNames
    }
}