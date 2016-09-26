package io.github.lujianbo.httpproxy.core.handler;

import io.github.lujianbo.httpproxy.core.util.ProxyUtil;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import org.apache.commons.lang3.StringUtils;


import java.util.regex.Pattern;

/**
 * Created by jianbo on 2016/9/26.
 */
public class ClientToProxyHandler extends SimpleChannelInboundHandler<HttpObject> {

    private Channel proxyToServer;

    public ClientToProxyHandler(Channel proxyToServer) {
        this.proxyToServer = proxyToServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject object) throws Exception {
        if (object instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) object;
            request.setUri(ProxyUtil.stripHost(request.getUri()));
            proxyToServer.writeAndFlush(request);
        }else {
            proxyToServer.writeAndFlush(object);
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
