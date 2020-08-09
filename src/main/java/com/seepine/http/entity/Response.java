package com.seepine.http.entity;

import com.seepine.http.enums.Header;
import com.seepine.http.util.FileUtil;
import com.seepine.http.util.IoUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * @author Seepine
 */
@Getter
@Setter
public class Response extends BaseHttp<Request> {
    int status;
    String url;

    public String getLocation() {
        return super.header(Header.LOCATION.toString());
    }

    public String getContentLength() {
        return super.header(Header.CONTENT_LENGTH.toString());
    }
}
