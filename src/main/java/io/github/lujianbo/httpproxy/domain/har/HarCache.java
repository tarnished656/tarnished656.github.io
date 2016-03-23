package io.github.lujianbo.httpproxy.domain.har;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarCache {

    /**
     * beforeRequest [object, optional] - State of a cache entry before the request. Leave out this field if the information is not available
     * */
    HarCacheObject beforeRequest =new HarCacheObject();

    /**
     * afterRequest [object, optional] - State of a cache entry after the request. Leave out this field if the information is not available
     * */
    HarCacheObject afterRequest =new HarCacheObject();

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment = "";

    public HarCacheObject getBeforeRequest() {
        return beforeRequest;
    }

    public void setBeforeRequest(HarCacheObject beforeRequest) {
        this.beforeRequest = beforeRequest;
    }

    public HarCacheObject getAfterRequest() {
        return afterRequest;
    }

    public void setAfterRequest(HarCacheObject afterRequest) {
        this.afterRequest = afterRequest;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
