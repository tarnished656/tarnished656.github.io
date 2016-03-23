package io.github.lujianbo.httpproxy.domain.har;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarParam {

    /**
     * name [string] - name of a posted parameter
     * */
    String name = "";

    /**
     * value [string, optional] - value of a posted parameter or content of a posted file
     * */
    String value = "";

    /**
     * fileName [string, optional] - name of a posted file
     * */
    String fileName = "";

    /**
     * contentType [string, optional] - content type of a posted file
     * */
    String contentType = "";

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment="";

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
