package io.github.lujianbo.httpproxy.domain.har;

import java.time.LocalDateTime;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarPage {

    /**
     * startedDateTime [string] - Date and time stamp for the beginning of the page load (ISO 8601 - YYYY-MM-DDThh:mm:ss.sTZD, e.g. 2009-07-24T19:20:30.45+01:00)
     * */
    LocalDateTime startedDateTime ;

    /**
     * id [string] - Unique identifier of a page within the <log>. Entries use it to refer the parent page
     * */
    String id = "";

    /**
     * title [string] - Page title
     * */
    String title = "";

    /**
     * pageTimings[object] - Detailed timing info about page load
     * */
    HarPageTimings pageTimings = new HarPageTimings();

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment = "";

    public LocalDateTime getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(LocalDateTime startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HarPageTimings getPageTimings() {
        return pageTimings;
    }

    public void setPageTimings(HarPageTimings pageTimings) {
        this.pageTimings = pageTimings;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
