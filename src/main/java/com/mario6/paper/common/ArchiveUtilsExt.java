package com.mario6.paper.common;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 压缩相关工具类
 */
public class ArchiveUtilsExt {
    public static final int BUFFER_SIZE = 1024;

    /**
     * 解压 zip 文件
     *
     * @param zipFile zip 压缩文件
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名的 list
     * @throws Exception
     */
    public static List<String> unZip(File zipFile, String destDir) {
        // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
        if (StringUtils.isBlank(destDir)) {
            destDir = zipFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        File destDirFile = new File(destDir);
        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }

        String zipPrefixPath = getZipPrefixPath(zipFile);

        ZipArchiveInputStream is = null;
        List<String> fileNames = new ArrayList<String>();

        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            ZipArchiveEntry entry = null;

            while ((entry = is.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());

                String contentFileName = entry.getName();
                if (zipPrefixPath != null) {
                    contentFileName = contentFileName.substring(zipPrefixPath.length());
                }

                if (entry.isDirectory()) {
                    File directory = new File(destDir, contentFileName);
                    directory.mkdirs();
                } else {
                    File targetFile = new File(destDir, contentFileName);
                    File targetDir = targetFile.getParentFile();
                    targetDir.mkdirs();
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }

        return fileNames;
    }

    /**
     * 解压 zip 文件
     *
     * @param zipfile zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名的 list
     * @throws Exception
     */
    public static List<String> unZip(String zipfile, String destDir) {
        File zipFile = new File(zipfile);
        return unZip(zipFile, destDir);
    }

    /**
     * 获取统一前缀
     *
     * @param zipFile
     * @return
     */
    private static String getZipPrefixPath(File zipFile) {
        List<String> paths = getZipContentFilePaths(zipFile);
        String prefix = null;
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            boolean hasPrefix = true;
            if (path.startsWith("__MACOSX")) continue;
            for (int j = i + 1; j < paths.size(); j++) {
                String path2 = paths.get(j);
                if (path2.startsWith("__MACOSX")) continue;
                if (!path2.startsWith(path)) {
                    hasPrefix = false;
                    break;
                }
            }
            if (hasPrefix) {
                prefix = path;
                break;
            }
        }
        return prefix;
    }

    /**
     * 获取指定zip压缩内容文件路径
     *
     * @param zipFile
     * @return
     */
    private static List<String> getZipContentFilePaths(File zipFile) {
        List<String> fileNames = new ArrayList<>();

        ZipArchiveInputStream is = null;
        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return fileNames;
    }

}
