package com.seepine.http.util;

import com.seepine.http.client.EasyHttpClient;
import com.seepine.http.entity.Request;
import com.seepine.http.entity.Response;
import com.seepine.http.entity.StreamProgress;
import com.seepine.http.enums.HttpConstant;
import com.seepine.http.lang.Assert;
import com.seepine.http.proxy.ProxyProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

/**
 * @author Seepine
 * @date 2020.8.5
 */
public class HttpUtil {
    public static final EasyHttpClient EASY_HTTP_CLIENT = new EasyHttpClient();

    public static void setGlobalProxy(ProxyProvider proxyProvider) {
        EASY_HTTP_CLIENT.setProxyProvider(proxyProvider);
    }

    /**
     * ************************************    get start ************************************
     */
    public static String get(String url) throws IOException {
        return execute(Request.build(url)).getBody();
    }

    public static String get(String url, Map<String, Object> map) throws IOException {
        return execute(Request.build(UrlUtil.getParamUrl(url, map))).getBody();
    }

    /**
     * ************************************    post start ************************************
     */
    public static String post(String url) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url)).getBody();
    }

    public static String post(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url).form(params)).getBody();
    }

    public static String post(String url, String jsonBody) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url).json(jsonBody)).getBody();
    }

    public static String postJson(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url).json(params)).getBody();
    }

    /**
     * ************************************    put start ************************************
     */
    public static String put(String url) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url)).getBody();
    }

    public static String put(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url).form(params)).getBody();
    }

    public static String put(String url, String jsonBody) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url).json(jsonBody)).getBody();
    }

    public static String putJson(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url).json(params)).getBody();
    }

    /**
     * ************************************    delete start ************************************
     */

    public static String delete(String url) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url)).getBody();
    }

    public static String delete(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url).form(params)).getBody();
    }

    public static String delete(String url, String jsonBody) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url).json(jsonBody)).getBody();
    }

    public static String deleteJson(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url).json(params)).getBody();
    }

    /**
     * ************************************    download start ************************************
     */
    public static void download(String url, OutputStream outputStream) throws IOException {
        download(url, outputStream, null);
    }

    public static void download(String url, OutputStream outputStream, StreamProgress streamProgress) throws IOException {
        execute(Request.build(url).binaryContent(true).destOutputStream(outputStream).streamProgress(streamProgress));
    }

    public static void downloadFile(String url, String destFile) throws IOException {
        downloadFile(url, destFile, null);
    }

    public static void downloadFile(String url, String destFile, StreamProgress streamProgress) throws IOException {
        Assert.isFile(destFile, "destFile path is a directory");
        execute(Request.build(url).binaryContent(true).destFile(destFile).streamProgress(streamProgress));
    }


    public static Response execute(Request request) throws IOException {
        return EASY_HTTP_CLIENT.execute(request);
    }

}
