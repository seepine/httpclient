package com.seepine.http.entity;

import com.alibaba.fastjson.JSON;
import com.seepine.http.enums.HttpConstant;
import com.seepine.http.lang.Assert;
import com.seepine.http.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.*;

/**
 * @author Seepine
 */
@Getter
@Setter
public class Request extends BaseHttp<Request> {
    private String method = HttpConstant.Method.GET;

    int timeOut = 20 * 1000;
    int retryTimes = 3;
    private boolean binaryContent = false;
    private boolean redirectsEnabled = true;
    private StreamProgress streamProgress;
    private String destFile;
    private OutputStream destOutputStream;

    private Request(String url) {
        this.url = url;
    }

    private Request(String method, String url) {
        this.method = method;
        this.url = url;
    }

    /**
     * @param retryTimes retryTimes
     * @return Request
     */
    public Request retryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    /**
     * @param outputStream outputStream
     * @return Request
     */
    public Request destOutputStream(OutputStream outputStream) {
        this.destOutputStream = outputStream;
        return this;
    }

    /**
     * @param destFile destFile
     * @return Request
     */
    public Request destFile(String destFile) {
        this.destFile = destFile;
        return this;
    }

    /**
     * @param streamProgress streamProgress
     * @return Request
     */
    public Request streamProgress(StreamProgress streamProgress) {
        this.streamProgress = streamProgress;
        return this;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        if (!Objects.equals(url, request.url)) {
            return false;
        }
        return Objects.equals(method, request.method);
    }

    /**
     * @param url url
     * @return Request
     */
    public static Request build(String url) {
        if (StrUtil.isBlank(url)) {
            throw new IllegalArgumentException("illegal url");
        }
        return new Request(url);
    }

    /**
     * @param method method
     * @param url    url
     * @return Request
     */
    public static Request build(String method, String url) {
        Assert.isNotBlank(method, "illegal method");
        Assert.isNotBlank(url, "illegal url");
        return new Request(method, url);
    }

    /**
     * @param enabled enabled
     * @return Request
     */
    public Request redirect(boolean enabled) {
        this.redirectsEnabled = enabled;
        return this;
    }

    public Request binaryContent(boolean enabled) {
        this.binaryContent = enabled;
        return this;
    }

    private Request body(byte[] body, String contentType, String encoding) {
        this.bodyBytes = body;
        this.contentType = contentType;
        this.encoding = encoding;
        return this;
    }

    /**
     * @param xml      xml
     * @param encoding encoding
     * @return Request
     */
    public Request xml(String xml, String encoding) {
        try {
            return body(xml.getBytes(encoding), HttpConstant.ContentType.XML, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }


    /**
     * @param json json
     * @return Request
     */
    public Request json(String json) {
        return json(json, this.encoding);
    }

    /**
     * json
     *
     * @param params params
     * @return Request
     */
    public Request json(Map<String, Object> params) {
        return json(JSON.toJSONString(params));
    }

    /**
     * json
     *
     * @param json     json
     * @param encoding encoding
     * @return Request
     */
    public Request json(String json, String encoding) {
        try {
            return body(json.getBytes(encoding), HttpConstant.ContentType.JSON, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    /**
     * form
     *
     * @param params params
     * @return Request
     */
    public Request form(Map<String, Object> params) {
        return form(params, this.encoding);
    }

    /**
     * form
     *
     * @param params   params
     * @param encoding encoding
     * @return Request
     */
    public Request form(Map<String, Object> params, String encoding) {
        List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        try {
            return body(URLEncodedUtils.format(nameValuePairs, encoding).getBytes(encoding), HttpConstant.ContentType.FORM, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }
}
