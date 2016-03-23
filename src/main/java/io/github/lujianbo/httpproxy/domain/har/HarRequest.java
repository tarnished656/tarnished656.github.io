package io.github.lujianbo.httpproxy.domain.har;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarRequest {

    /**
     * method [string] - Request method (GET, POST, ...)
     * */
    String method = "";

    /**
     * url [string] - Absolute URL of the request (fragments are not included)
     * */
    String url = "";

    /**
     * httpVersion [string] - Request HTTP Version
     * */
    String httpVersion = "";

    /**
     * cookies [array] - List of cookie objects
     * */
    List<HarCookie> cookies = new CopyOnWriteArrayList<>();

    /**
     * headers [array] - List of header objects
     * */
    List<HarHeader> headers = new CopyOnWriteArrayList<>();

    /**
     * queryString [array] - List of query parameter objects
     * */
    List<HarQueryParam> queryString = new CopyOnWriteArrayList<>();

    /**
     * postData [object, optional] - Posted data info
     * */
    HarPostData postData = new HarPostData();

    /**
     * headersSize [number] - Total number of bytes from the start of the HTTP request message until (and including) the double CRLF before the body. Set to -1 if the info is not available
     * */
    long headersSize = -1;

    /**
     * bodySize [number] - Size of the request body (POST data payload) in bytes. Set to -1 if the info is not available
     * */
    long bodySize = -1;

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment="";

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public List<HarCookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<HarCookie> cookies) {
        this.cookies = cookies;
    }

    public List<HarHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HarHeader> headers) {
        this.headers = headers;
    }

    public List<HarQueryParam> getQueryString() {
        return queryString;
    }

    public void setQueryString(List<HarQueryParam> queryString) {
        this.queryString = queryString;
    }

    public HarPostData getPostData() {
        return postData;
    }

    public void setPostData(HarPostData postData) {
        this.postData = postData;
    }

    public long getHeadersSize() {
        return headersSize;
    }

    public void setHeadersSize(long headersSize) {
        this.headersSize = headersSize;
    }

    public long getBodySize() {
        return bodySize;
    }

    public void setBodySize(long bodySize) {
        this.bodySize = bodySize;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
