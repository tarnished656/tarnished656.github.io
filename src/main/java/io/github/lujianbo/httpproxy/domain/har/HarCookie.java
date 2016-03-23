package io.github.lujianbo.httpproxy.domain.har;

import java.time.LocalDateTime;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarCookie {

    /**
     * name [string] - The name of the cookie
     * */
    String name = "";

    /**
     * value [string] - The cookie value
     * */
    String value = "";

    /**
     * path [string, optional] - The path pertaining to the cookie
     * */
    String path = "";

    /**
     * domain [string, optional] - The host of the cookie
     * */
    String domain = "";

    /**
     * expires [string, optional] - Cookie expiration time. (ISO 8601 - YYYY-MM-DDThh:mm:ss.sTZD, e.g. 2009-07-24T19:20:30.123+02:00)
     * */
    LocalDateTime expires;

    /**
     * httpOnly [boolean, optional] - Set to true if the cookie is HTTP only, false otherwise
     * */
    boolean httpOnly=false;

    /**
     * secure [boolean, optional] (new in 1.2) - True if the cookie was transmitted over ssl, false otherwise
     * */
    boolean secure=false;

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
