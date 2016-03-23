package io.github.lujianbo.httpproxy.domain.har;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarLog {

    /**
     * version [string] - Version number of the format. If empty, string "1.1" is assumed by default
     * */
    String version="1.2";

    /**
     * creator [object] - Name and version info of the log creator application
     * */
    HarCreator creator = new HarCreator();

    /**
     * browser [object, optional] - Name and version info of used browser
     * */
    HarBrowser browser = new HarBrowser();

    /**
     * pages [array, optional] - List of all exported (tracked) pages. Leave out this field if the application does not support grouping by pages
     * */
    List<HarPage> pages = new CopyOnWriteArrayList<>();

    /**
     * entries [array] - List of all exported (tracked) requests
     * */
    List<HarEntry> entries = new CopyOnWriteArrayList<>();

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment="";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HarCreator getCreator() {
        return creator;
    }

    public void setCreator(HarCreator creator) {
        this.creator = creator;
    }

    public HarBrowser getBrowser() {
        return browser;
    }

    public void setBrowser(HarBrowser browser) {
        this.browser = browser;
    }

    public List<HarPage> getPages() {
        return pages;
    }

    public void setPages(List<HarPage> pages) {
        this.pages = pages;
    }

    public List<HarEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<HarEntry> entries) {
        this.entries = entries;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
