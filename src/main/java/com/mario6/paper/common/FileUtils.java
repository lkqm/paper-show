package com.mario6.paper.common;


import java.io.File;

/**
 * 文件操作相关工具类
 *
 * @author Mario Luo
 * @date 2018.08.09 17:19
 */
public class FileUtils {

    /**
     * 生成指定文件的父目录
     */
    public static boolean makeParent(String file) {
        File fileObj = new File(file);
        return makeParent(fileObj);
    }

    /**
     * 生成指定文件的父目录
     */
    public static boolean makeParent(File file) {
        boolean success = true;
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                success = parentFile.mkdirs();
            }
            // 创建失败可能由于权限等问题
            if (!parentFile.exists()) {
                success = false;
            }
        }
        return success;
    }

    /**
     * 删除指定文件夹
     *
     * @param dir
     * @return
     */
    public static int deleteDir(String dir) {
        return deleteDir(new File(dir));
    }

    /**
     * 删除指定文件夹
     *
     * @param dir
     * @return
     */
    public static int deleteDir(File dir) {
        int count = 0;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                count += deleteDir(new File(dir, children[i]));
            }
            dir.delete();
        } else {
            boolean delete = dir.delete();
            if (delete) count++;
        }
        return count;
    }

    /**
     * 清空目录, 不删除目录本身
     *
     * @param dir
     * @return
     */
    public static int emptyDir(String dir) {
        return emptyDir(new File(dir));
    }

    /**
     * 清空目录, 不删除目录本身
     *
     * @param dir
     * @return
     */
    private static int emptyDir(File dir) {
        int count = 0;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                count += deleteDir(new File(dir, children[i]));
            }
        }
        return count;
    }
}
