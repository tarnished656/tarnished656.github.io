package io.github.lujianbo.httpproxy.domain.har;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarPostData {

    /**
     * mimeType [string] - Mime type of posted data
     * */
    String mimeType = "";

    /**
     * params [array] - List of posted parameters (in case of URL encoded parameters)
     * */
    List<HarParam> params = new CopyOnWriteArrayList<>();

    /**
     * text [string] - Plain text posted data
     * */
    String text = "";

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment="";

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<HarParam> getParams() {
        return params;
    }

    public void setParams(List<HarParam> params) {
        this.params = params;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
