package com.seepine.http.util;

import com.seepine.http.client.EasyHttpClient;
import com.seepine.http.entity.Request;
import com.seepine.http.entity.Response;
import com.seepine.http.entity.StreamProgress;
import com.seepine.http.enums.HttpConstant;
import com.seepine.http.lang.Assert;
import com.seepine.http.proxy.ProxyProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Seepine
 */
public class HttpUtil {
    public static final EasyHttpClient EASY_HTTP_CLIENT = new EasyHttpClient();
    public static void setGlobalProxy(ProxyProvider proxyProvider) {
        EASY_HTTP_CLIENT.setProxyProvider(proxyProvider);
    }
    //**************************************    get start *************************************
    /**
     * @param url url
     * @return String
     * @throws IOException IOException
     */
    public static String get(String url) throws IOException {
        return execute(Request.build(url)).getBody();
    }

    /**
     * @param url url
     * @param map map
     * @return String
     * @throws IOException IOException
     */
    public static String get(String url, Map<String, Object> map) throws IOException {
        return execute(Request.build(UrlUtil.getParamUrl(url, map))).getBody();
    }

    //************************************    post start ************************************

    /**
     * @param url url
     * @return String
     * @throws IOException IOException
     */
    public static String post(String url) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url)).getBody();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     * @throws IOException IOException
     */
    public static String post(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url).form(params)).getBody();
    }

    /**
     * @param url      url
     * @param jsonBody jsonBody
     * @return String
     * @throws IOException IOException
     */
    public static String post(String url, String jsonBody) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url).json(jsonBody)).getBody();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     * @throws IOException IOException
     */
    public static String postJson(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.POST, url).json(params)).getBody();
    }

    //************************************    put start ************************************

    /**
     * @param url url
     * @return String
     * @throws IOException IOException
     */
    public static String put(String url) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url)).getBody();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     * @throws IOException String
     */
    public static String put(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url).form(params)).getBody();
    }

    /**
     * @param url      url
     * @param jsonBody jsonBody
     * @return String
     * @throws IOException IOException
     */
    public static String put(String url, String jsonBody) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url).json(jsonBody)).getBody();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     * @throws IOException IOException
     */
    public static String putJson(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.PUT, url).json(params)).getBody();
    }

    //************************************    delete start ************************************

    /**
     * @param url url
     * @return String
     * @throws IOException IOException
     */
    public static String delete(String url) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url)).getBody();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     * @throws IOException IOException
     */
    public static String delete(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url).form(params)).getBody();
    }

    /**
     * @param url      url
     * @param jsonBody jsonBody
     * @return String
     * @throws IOException IOException
     */
    public static String delete(String url, String jsonBody) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url).json(jsonBody)).getBody();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     * @throws IOException IOException
     */
    public static String deleteJson(String url, Map<String, Object> params) throws IOException {
        return execute(Request.build(HttpConstant.Method.DELETE, url).json(params)).getBody();
    }

    //************************************    download start ************************************

    /**
     * @param url          url
     * @param outputStream outputStream
     * @throws IOException IOException
     */
    public static void download(String url, OutputStream outputStream) throws IOException {
        download(url, outputStream, null);
    }

    /**
     * @param url            url
     * @param outputStream   outputStream
     * @param streamProgress streamProgress
     * @throws IOException IOException
     */
    public static void download(String url, OutputStream outputStream, StreamProgress streamProgress) throws IOException {
        execute(Request.build(url).binaryContent(true).destOutputStream(outputStream).streamProgress(streamProgress));
    }

    /**
     * @param url      url
     * @param destFile destFile
     * @throws IOException IOException
     */
    public static void downloadFile(String url, String destFile) throws IOException {
        downloadFile(url, destFile, null);
    }

    /**
     * @param url            url
     * @param destFile       destFile
     * @param streamProgress streamProgress
     * @throws IOException IOException
     */
    public static void downloadFile(String url, String destFile, StreamProgress streamProgress) throws IOException {
        Assert.isFile(destFile, "destFile path is a directory");
        execute(Request.build(url).binaryContent(true).destFile(destFile).streamProgress(streamProgress));
    }

    /**
     * @param request request
     * @return Response
     * @throws IOException IOException
     */
    public static Response execute(Request request) throws IOException {
        return EASY_HTTP_CLIENT.execute(request);
    }

}
