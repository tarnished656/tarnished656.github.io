package com.lujianbo.app.httpproxy.core.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

/**
 * Created by jianbo on 2016/9/26.
 */
public class HTTPProxyHandler extends SimpleChannelInboundHandler<HttpObject> {

    private Channel proxyToServer;

    public HTTPProxyHandler(Channel proxyToServer) {
        this.proxyToServer = proxyToServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject object) throws Exception {
        if (object instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) object;
            request.setUri(ProxyUtil.stripHost(request.uri()));
            proxyToServer.writeAndFlush(request);
        }else {
            if (object instanceof LastHttpContent){
                proxyToServer.writeAndFlush(object);
                proxyToServer.write(Unpooled.EMPTY_BUFFER);
            }else {
                proxyToServer.writeAndFlush(object);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (null != proxyToServer && proxyToServer.isActive()) {
            ProxyUtil.closeOnFlush(proxyToServer);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ProxyUtil.closeOnFlush(proxyToServer);
    }

}
