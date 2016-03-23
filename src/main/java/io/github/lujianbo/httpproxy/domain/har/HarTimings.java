package io.github.lujianbo.httpproxy.domain.har;

/**
 * Created by jianbo on 2016/3/23.
 */
public class HarTimings {
    /**
     * blocked [number, optional] - Time spent in a queue waiting for a network connection. Use -1 if the timing does not apply to the current request
     * */
    long blocked=-1;

    /**
     * dns [number, optional] - DNS resolution time. The time required to resolve a host name. Use -1 if the timing does not apply to the current request
     * */
    long dns = -1;

    /**
     * connect [number, optional] - Time required to create TCP connection. Use -1 if the timing does not apply to the current request
     * */
    long connect = -1;

    /**
     * send [number] - Time required to send HTTP request to the server
     * */
    long send = 0;

    /**
     * wait [number] - Waiting for a response from the server
     * */
    long wait = 0;

    /**
     * receive [number] - Time required to read entire response from the server (or cache)
     * */
    long receive = 0;

    /**
     * ssl [number, optional] (new in 1.2) - Time required for SSL/TLS negotiation. If this field is defined then the time is also included in the connect field (to ensure backward compatibility with HAR 1.1). Use -1 if the timing does not apply to the current request
     * */
    long ssl = -1;

    /**
     * comment [string, optional] (new in 1.2) - A comment provided by the user or the application
     * */
    String comment;

    public long getBlocked() {
        return blocked;
    }

    public void setBlocked(long blocked) {
        this.blocked = blocked;
    }

    public long getDns() {
        return dns;
    }

    public void setDns(long dns) {
        this.dns = dns;
    }

    public long getConnect() {
        return connect;
    }

    public void setConnect(long connect) {
        this.connect = connect;
    }

    public long getSend() {
        return send;
    }

    public void setSend(long send) {
        this.send = send;
    }

    public long getWait() {
        return wait;
    }

    public void setWait(long wait) {
        this.wait = wait;
    }

    public long getReceive() {
        return receive;
    }

    public void setReceive(long receive) {
        this.receive = receive;
    }

    public long getSsl() {
        return ssl;
    }

    public void setSsl(long ssl) {
        this.ssl = ssl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
