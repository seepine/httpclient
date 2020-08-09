package com.seepine.http.entity;

import com.seepine.http.enums.Header;
import com.seepine.http.util.FileUtil;
import com.seepine.http.util.IoUtil;
import com.seepine.http.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author Seepine
 */
@Getter
@Setter
public class Response extends HttpBase<Request> implements Closeable {
    int status;
    String url;
    /**
     * Http请求原始流
     */
    protected InputStream in;

    public String getLocation() {
        return super.header(Header.LOCATION.toString());
    }

    public String getContentLength() {
        return super.header(Header.CONTENT_LENGTH.toString());
    }

    @Override
    public void close() throws IOException {
        IoUtil.close(in);
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param destFilePath 写出到的文件的路径
     * @return 写出bytes数
     * @since 3.3.2
     */
    public long writeBody(String destFilePath) {
        return writeBody(FileUtil.touch(destFilePath));
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param destFilePath 写出到的文件的路径
     * @return 写出bytes数
     * @since 3.3.2
     */
    public long writeBody(String destFilePath, StreamProgress streamProgress) {
        return writeBody(FileUtil.touch(destFilePath), streamProgress);
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param destFile 写出到的文件
     * @return 写出bytes数
     * @since 3.3.2
     */
    public long writeBody(File destFile) {
        return writeBody(destFile, null);
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param destFile       写出到的文件
     * @param streamProgress 进度显示接口，通过实现此接口显示下载进度
     * @return 写出bytes数
     * @since 3.3.2
     */
    public long writeBody(File destFile, StreamProgress streamProgress) {
        if (null == destFile) {
            throw new NullPointerException("[destFile] is null!");
        }
        if (destFile.isDirectory()) {
            throw new IllegalArgumentException("destFile is a directory");
        }

        return writeBody(FileUtil.getOutputStream(destFile), true, streamProgress);
    }

    public long writeBody(OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
        if (null == out) {
            throw new NullPointerException("[out] is null!");
        }
        try {
            return IoUtil.copyByNIO(in, out, IoUtil.DEFAULT_BUFFER_SIZE, streamProgress);
        } finally {
            IoUtil.close(this);
            if (isCloseOut) {
                IoUtil.close(out);
            }
        }
    }
}
