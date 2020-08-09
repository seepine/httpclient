package com.seepine.http.util;

import com.seepine.http.entity.StreamProgress;
import com.seepine.http.exception.IoRuntimeException;

import java.io.*;

/**
 * @author Seepine
 */
public class FileUtil {

    /**
     * 创建所给文件或目录的父目录
     *
     * @param file 文件或目录
     * @return 父目录
     */
    public static File mkParentDirs(File file) {
        final File parentFile = file.getParentFile();
        if (null != parentFile && !parentFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parentFile.mkdirs();
        }
        return parentFile;
    }

    /**
     * 创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
     *
     * @param path 文件路径
     * @return File
     */
    public static File touch(String path) {
        if (StrUtil.isBlank(path)) {
            return null;
        }
        return touch(new File(path));
    }

    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param file 文件对象
     * @return 文件，若路径为null，返回null
     * @throws IoRuntimeException IO异常
     */
    public static File touch(File file) throws IoRuntimeException {
        if (null == file) {
            throw new IllegalArgumentException("ill file path");
        }
        if (!file.exists()) {
            if (mkParentDirs(file) != null) {
                try {
                    if (!file.createNewFile()) {
                        throw new IllegalArgumentException("ill create new file");
                    }
                } catch (Exception e) {
                    throw new IoRuntimeException(e);
                }
            }
        }
        return file;
    }

    /**
     * 获得一个输出流对象
     *
     * @param file 文件
     * @return 输出流对象
     * @throws IoRuntimeException IO异常
     */
    public static BufferedOutputStream getOutputStream(File file) throws IoRuntimeException {
        try {
            return new BufferedOutputStream(new FileOutputStream(touch(file)));
        } catch (Exception e) {
            throw new IoRuntimeException(e);
        }
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param in           in
     * @param destFilePath 写出到的文件的路径
     * @return 写出bytes数
     * @since 3.3.2
     */
    public static long writeBody(InputStream in, String destFilePath) {
        return writeBody(in, FileUtil.touch(destFilePath));
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param in       in
     * @param destFile 写出到的文件
     * @return 写出bytes数
     * @since 3.3.2
     */
    public static long writeBody(InputStream in, File destFile) {
        return writeBody(in, destFile, null);
    }

    /**
     * @param in             in
     * @param filePath       filePath
     * @param streamProgress streamProgress
     * @return long
     */
    public static long writeBody(InputStream in, String filePath, StreamProgress streamProgress) {
        return writeBody(in, FileUtil.touch(filePath), streamProgress);
    }

    /**
     * @param in             in
     * @param destFile       in
     * @param streamProgress streamProgress
     * @return long
     */
    public static long writeBody(InputStream in, File destFile, StreamProgress streamProgress) {
        if (null == destFile) {
            throw new NullPointerException("[destFile] is null!");
        }
        if (destFile.isDirectory()) {
            throw new IllegalArgumentException("destFile is a directory");
        }
        return writeBody(in, FileUtil.getOutputStream(destFile), true, streamProgress);
    }

    /**
     * @param in             in
     * @param out            out
     * @param isCloseOut     isCloseOut
     * @param streamProgress streamProgress
     * @return long
     */
    public static long writeBody(InputStream in, OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
        if (null == out) {
            throw new NullPointerException("[out] is null!");
        }
        try {
            return IoUtil.copyByNio(in, out, IoUtil.DEFAULT_BUFFER_SIZE, streamProgress);
        } finally {
            if (isCloseOut) {
                IoUtil.close(out);
            }
        }
    }
}
