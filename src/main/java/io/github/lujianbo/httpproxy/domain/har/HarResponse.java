package io.github.lujianbo.httpproxy.domain.har;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarResponse {

    /**
     * status [number] - Response status
     * */
    int status;

    /**
     * statusText [string] - Response status description
     * */
    String statusText = "";

    /**
     * httpVersion [string] - Response HTTP Version
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
     * content [object] - Details about the response body
     * */
    HarContent content = new HarContent();

    /**
     * redirectURL [string] - Redirection target URL from the Location response header
     * */
    String redirectURL = "";

    /**
     * headersSize [number]* - Total number of bytes from the start of the HTTP response message until (and including) the double CRLF before the body. Set to -1 if the info is not available
     * */
    long headersSize = -1;

    /**
     * bodySize [number] - Size of the received response body in bytes. Set to zero in case of responses coming from the cache (304). Set to -1 if the info is not available
     * */
    long bodySize = -1;

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment = "";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    public HarContent getContent() {
        return content;
    }

    public void setContent(HarContent content) {
        this.content = content;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
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
