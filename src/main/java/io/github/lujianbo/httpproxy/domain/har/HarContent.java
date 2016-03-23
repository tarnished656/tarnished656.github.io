package io.github.lujianbo.httpproxy.domain.har;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarContent {

    /**
     * size [number] - Length of the returned content in bytes. Should be equal to response.bodySize if there is no compression and bigger when the content has been compressed
     * */
    long size = 0;
    /**
     * compression [number, optional] - Number of bytes saved. Leave out this field if the information is not available
     * */
    long compression = 0;
    /**
     * mimeType [string] - MIME type of the response text (value of the Content-Type response header). The charset attribute of the MIME type is included (if available)
     * */
    String mimeType = "";
    /**
     * text [string, optional] - Response body sent from the server or loaded from the browser cache. This field is populated with textual content only. The text field is either HTTP decoded text or a encoded (e.g. "base64") representation of the response body. Leave out this field if the information is not available
     * */
    String text = "";
    /**
     * encoding [string, optional] (new in 1.2) - Encoding used for response text field e.g "base64". Leave out this field if the text field is HTTP decoded (decompressed & unchunked), than trans-coded from its original character set into UTF-8
     * */
    String encoding = "";

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCompression() {
        return compression;
    }

    public void setCompression(long compression) {
        this.compression = compression;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
