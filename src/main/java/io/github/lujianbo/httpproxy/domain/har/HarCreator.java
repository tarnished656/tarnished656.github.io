package io.github.lujianbo.httpproxy.domain.har;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarCreator {

    /**
     * name [string] - Name of the application/browser used to export the log
     * */
    String name = "";

    /**
     * version [string] - Version of the application/browser used to export the log
     * */
    String version = "";

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
