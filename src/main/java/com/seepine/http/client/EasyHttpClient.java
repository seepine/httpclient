package com.seepine.http.client;

import com.seepine.http.proxy.Proxy;
import com.seepine.http.entity.Request;
import com.seepine.http.entity.Response;
import com.seepine.http.proxy.ProxyProvider;
import com.seepine.http.proxy.SimpleProxyProvider;
import com.seepine.http.util.FileUtil;
import com.seepine.http.util.IoUtil;
import com.seepine.http.util.StrUtil;
import com.seepine.http.util.UrlUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Seepine
 */
public class EasyHttpClient {
    private final Map<String, CloseableHttpClient> httpClientMap = new HashMap<>();
    private final HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private ProxyProvider proxyProvider = SimpleProxyProvider.from();

    public void setProxyProvider(ProxyProvider proxyProvider) {
        if (proxyProvider == null) {
            this.proxyProvider = SimpleProxyProvider.from();
        } else {
            this.proxyProvider = proxyProvider;
        }
    }

    private CloseableHttpClient getHttpClient(String url) {
        String domain = UrlUtil.getDomain(url);
        CloseableHttpClient httpClient = httpClientMap.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClientMap.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient();
                    httpClientMap.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    public Response execute(Request request) throws IOException {
        CloseableHttpResponse httpResponse;
        CloseableHttpClient httpClient = getHttpClient(request.getUrl());
        Response res = null;
        Proxy proxy = proxyProvider.getProxy();
        httpResponse = httpClient.execute(HttpUriRequestConverter.convertHttpUriRequest(request, proxy), HttpUriRequestConverter.convertHttpClientContext(proxy));
        res = handleResponse(request, request.charset() != null ? request.charset() : request.charset(), httpResponse);
        EntityUtils.consumeQuietly(httpResponse.getEntity());
        return res;
    }


    private Response handleResponse(Request request, String charset, HttpResponse httpResponse) throws IOException {
        Response res = new Response();
        res.setContentType(httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue());
        res.setUrl(request.getUrl());
        res.setStatus(httpResponse.getStatusLine().getStatusCode());
        res.header(convertHeaders(httpResponse.getAllHeaders()));
        if (request.isBinaryContent()) {
            if (request.getStreamProgress() != null) {
                long cl = -1;
                String contentLength = res.getContentLength();
                if (StrUtil.isNotBlank(contentLength)) {
                    cl = Long.parseLong(contentLength);
                }
                request.getStreamProgress().startBefore(cl);
            }
            if (StrUtil.isNotBlank(request.getDestFile())) {
                FileUtil.writeBody(httpResponse.getEntity().getContent(), request.getDestFile(), request.getStreamProgress());
            } else {
                FileUtil.writeBody(httpResponse.getEntity().getContent(), request.getDestOutputStream(), true, request.getStreamProgress());
            }
        } else {
            byte[] bytes = IoUtil.toByteArray(httpResponse.getEntity().getContent());
            res.charset(charset);
            res.setBody(new String(bytes, charset));
            res.setBodyBytes(bytes);
        }
        // System.out.println(StrUtil.CRLF+"Status Code:" + res.getStatus() + StrUtil.CRLF);
        return res;
    }

    private static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> results = new HashMap<>(headers.length);
        for (Header header : headers) {
            results.put(header.getName(), header.getValue());
        }
        return results;
    }
}
