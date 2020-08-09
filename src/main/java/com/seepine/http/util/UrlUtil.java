package com.seepine.http.util;

import com.seepine.http.entity.Request;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Seepine
 */
public class UrlUtil {
    /**
     * canonicalizeUrl
     * <br>
     * Borrowed from Jsoup.
     *
     * @param url   url
     * @param refer refer
     * @return canonicalizeUrl
     */
    public static String canonicalizeUrl(String url, String refer) {
        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its own, so try that
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
            if (url.startsWith(StrUtil.QUESTION_MARK)) {
                url = base.getPath() + url;
            }
            URL abs = new URL(base, url);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return StrUtil.EMPTY;
        }
    }

    /**
     * @param url url
     * @return new url
     * @deprecated
     */
    public static String encodeIllegalCharacterInUrl(String url) {
        return url.replace(" ", "%20");
    }

    /**
     * @param url url
     * @return String
     */
    public static String fixIllegalCharacterInUrl(String url) {
        //TODO more charator support
        return url.replace(" ", "%20").replaceAll("#+", "#");
    }

    private static Pattern patternForProtocal = Pattern.compile("[\\w]+://");

    public static String removeProtocol(String url) {
        return patternForProtocal.matcher(url).replaceAll("");
    }

    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = StrUtil.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = domain.substring(0, i);
        }
        return removePort(domain);
    }

    public static String removePort(String domain) {
        int portIndex = domain.indexOf(":");
        if (portIndex != -1) {
            return domain.substring(0, portIndex);
        } else {
            return domain;
        }
    }

    public static List<Request> convertToRequests(Collection<String> urls) {
        List<Request> requestList = new ArrayList<Request>(urls.size());
        for (String url : urls) {
            requestList.add(Request.build(url));
        }
        return requestList;
    }

    public static List<String> convertToUrls(Collection<Request> requests) {
        List<String> urlList = new ArrayList<String>(requests.size());
        for (Request request : requests) {
            urlList.add(request.getUrl());
        }
        return urlList;
    }

    private static final Pattern PATTERN_FOR_CHARSET = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)", Pattern.CASE_INSENSITIVE);

    public static String getCharset(String contentType) {
        Matcher matcher = PATTERN_FOR_CHARSET.matcher(contentType);
        if (matcher.find()) {
            String charset = matcher.group(1);
            if (Charset.isSupported(charset)) {
                return charset;
            }
        }
        return null;
    }

    public static String getParamUrl(String url, Map<String, Object> param) {
        if (param != null && param.size() > 0) {
            StringBuilder urlBuilder = new StringBuilder(url).append(StrUtil.QUESTION_MARK);
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                urlBuilder.append(entry.getKey()).append(StrUtil.EQUAL_MARK).append(entry.getValue()).append(StrUtil.AND_MARK);
            }
            return urlBuilder.substring(0, urlBuilder.length() - 1);
        }
        return url;
    }
}