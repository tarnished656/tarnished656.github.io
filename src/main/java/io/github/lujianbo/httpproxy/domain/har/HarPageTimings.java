package io.github.lujianbo.httpproxy.domain.har;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarPageTimings {

    /**
     * onContentLoad [number, optional] - Content of the page loaded. Number of milliseconds since page load started (page.startedDateTime). Use -1 if the timing does not apply to the current request
     * */
    long onContentLoad = -1;

    /**
     * onLoad [number,optional] - Page is loaded (onLoad event fired). Number of milliseconds since page load started (page.startedDateTime). Use -1 if the timing does not apply to the current request
     * */
    long onLoad = -1;

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment;

    public long getOnContentLoad() {
        return onContentLoad;
    }

    public void setOnContentLoad(long onContentLoad) {
        this.onContentLoad = onContentLoad;
    }

    public long getOnLoad() {
        return onLoad;
    }

    public void setOnLoad(long onLoad) {
        this.onLoad = onLoad;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
