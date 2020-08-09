package com.seepine.http.entity;

import com.seepine.http.enums.Header;
import com.seepine.http.enums.HttpConstant;
import com.seepine.http.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author Seepine
 */
@Setter
@Getter
@SuppressWarnings("unchecked")
public abstract class HttpBase<T> implements Serializable {
    private static final long serialVersionUID = 2062192774891352043L;
    /**
     * 存储头信息
     */
    protected Map<String, String> headers = new HashMap<>();
    /**
     * 编码
     */
    protected Charset charset = StandardCharsets.UTF_8;
    /**
     * 存储主体
     */
    protected byte[] bodyBytes;

    protected String encoding = "UTF-8";

    protected String contentType = HttpConstant.ContentType.JSON;
    protected String url;

    public Map<String, String> header() {
        return headers;
    }

    protected String body;

    public String header(String name) {
        if (name == null || headers.isEmpty()) {
            return null;
        }
        return headers.get(name);
    }

    public T header(String name, String value) {
        headers.put(name.trim(), value);
        return (T) this;
    }

    public T header(Header name, String value) {
        return header(name.toString(), value);
    }

    public T header(Map<String, String> headers) {
        if (null == headers || headers.isEmpty()) {
            return (T) this;
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.header(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    public String charset() {
        return charset.name();
    }

    public T charset(String charset) {
        if (charset != null && !"".equals(charset)) {
            this.charset = Charset.forName(charset);
        }
        return (T) this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Headers: ").append(StrUtil.CRLF);
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            sb.append("    ").append(
                    entry.getKey()).append(": ").append(entry.getValue()).append(",")
                    .append(StrUtil.CRLF);
        }
        sb.append("Body: ").append(StrUtil.CRLF);
        sb.append("    ").append(body).append(StrUtil.CRLF);
        return sb.toString();
    }
}
