package com.github.lkqm.paper.util

import java.io.File

/**
 * 文件操作相关工具类
 *
 * @author Mario Luo
 * @date 2018.08.09 17:19
 */
object FileUtils {
    /**
     * 生成指定文件的父目录
     */
    fun makeParent(file: String?): Boolean {
        val fileObj = File(file)
        return makeParent(fileObj)
    }

    /**
     * 生成指定文件的父目录
     */
    fun makeParent(file: File): Boolean {
        var success = true
        if (!file.exists()) {
            val parentFile = file.parentFile
            if (!parentFile.exists()) {
                success = parentFile.mkdirs()
            }
            // 创建失败可能由于权限等问题
            if (!parentFile.exists()) {
                success = false
            }
        }
        return success
    }

    /**
     * 删除指定文件夹
     *
     * @param dir
     * @return
     */
    fun deleteDir(dir: String?): Int {
        return deleteDir(File(dir))
    }

    /**
     * 删除指定文件夹
     *
     * @param dir
     * @return
     */
    fun deleteDir(dir: File): Int {
        var count = 0
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                count += deleteDir(File(dir, children[i]))
            }
            dir.delete()
        } else {
            val delete = dir.delete()
            if (delete) count++
        }
        return count
    }

    /**
     * 清空目录, 不删除目录本身
     *
     * @param dir
     * @return
     */
    fun emptyDir(dir: String?): Int {
        return emptyDir(File(dir))
    }

    /**
     * 清空目录, 不删除目录本身
     *
     * @param dir
     * @return
     */
    private fun emptyDir(dir: File): Int {
        var count = 0
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                count += deleteDir(File(dir, children[i]))
            }
        }
        return count
    }
}