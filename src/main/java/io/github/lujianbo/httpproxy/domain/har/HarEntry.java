package io.github.lujianbo.httpproxy.domain.har;

import java.time.LocalDateTime;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarEntry {

    /**
     * pageref [string, unique, optional] - Reference to the parent page. Leave out this field if the application does not support grouping by pages
     * */
    String pageref;

    /**
     * startedDateTime [string] - Date and time stamp of the request start (ISO 8601 - YYYY-MM-DDThh:mm:ss.sTZD)
     * */
    LocalDateTime startedDateTime;

    /**
     * time [number] - Total elapsed time of the request in milliseconds. This is the sum of all timings available in the timings object (i.e. not including -1 values)
     * */
    long time;

    /**
     * request [object] - Detailed info about the request
     * */
    HarRequest request =new HarRequest();

    /**
     * response [object] - Detailed info about the response
     * */
    HarResponse response =new HarResponse();

    /**
     * cache [object] - Info about cache usage
     * */
    HarCache cache = new HarCache();

    /**
     * timings [object] - Detailed timing info about request/response round trip
     * */
    HarTimings timings = new HarTimings();

    /**
     * serverIPAddress [string, optional] (new in 1.2) - IP address of the server that was connected (result of DNS resolution)
     * */
    String serverIPAddress = "";

    /**
     * connection [string, optional] (new in 1.2) - Unique ID of the parent TCP/IP connection, can be the client or server port number. Note that a port number doesn't have to be unique identifier in cases where the port is shared for more connections. If the port isn't available for the application, any other unique connection ID can be used instead (e.g. connection index). Leave out this field if the application doesn't support this info
     * */
    String connection = "";

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment = "";


    public String getPageref() {
        return pageref;
    }

    public void setPageref(String pageref) {
        this.pageref = pageref;
    }

    public LocalDateTime getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(LocalDateTime startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public HarRequest getRequest() {
        return request;
    }

    public void setRequest(HarRequest request) {
        this.request = request;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public HarResponse getResponse() {
        return response;
    }

    public void setResponse(HarResponse response) {
        this.response = response;
    }

    public HarCache getCache() {
        return cache;
    }

    public void setCache(HarCache cache) {
        this.cache = cache;
    }

    public HarTimings getTimings() {
        return timings;
    }

    public void setTimings(HarTimings timings) {
        this.timings = timings;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
