package io.github.lujianbo.httpproxy.domain.har;

import java.time.LocalDateTime;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarCacheObject {

    /**
     * expires [string, optional] - Expiration time of the cache entry
     * */
    LocalDateTime expires;

    /**
     * lastAccess [string] - The last time the cache entry was opened
     * */
    LocalDateTime lastAccess;

    /**
     * eTag [string] - Etag
     * */
    String eTag;

    /**
     * hitCount [number] - The number of times the cache entry has been opened
     * */
    long hitCount;

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment;

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
