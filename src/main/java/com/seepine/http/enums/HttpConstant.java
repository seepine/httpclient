package com.seepine.http.enums;

/**
 * @author Seepine
 */
public interface HttpConstant {
    interface Method {
        String GET = "GET";
        String HEAD = "HEAD";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
        String TRACE = "TRACE";
        String CONNECT = "CONNECT";
    }

    interface ContentType {
        String JSON = "application/json";
        String XML = "text/xml";
        String FORM = "application/x-www-form-urlencoded";
        String MULTIPART = "multipart/form-data";
    }
}
