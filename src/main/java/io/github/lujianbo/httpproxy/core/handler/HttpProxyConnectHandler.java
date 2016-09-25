package io.github.lujianbo.httpproxy.core.handler;

import io.github.lujianbo.httpproxy.core.util.ProxyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.Promise;

import java.net.URI;

public class HttpProxyConnectHandler extends SimpleChannelInboundHandler<HttpObject> {

    private Channel outboundChannel;

    public HttpProxyConnectHandler(Channel outboundChannel) {
        this.outboundChannel=outboundChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject object) throws Exception {
        System.out.println(object);
        if (object instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) object;
            URI uri = new URI(request.getUri());
            request.setUri(uri.getPath());
            System.out.println(request);
            outboundChannel.writeAndFlush(request);
        }else {
            outboundChannel.writeAndFlush(object);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (null!=outboundChannel&&outboundChannel.isActive()) {
            ProxyUtil.closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ProxyUtil.closeOnFlush(outboundChannel);
    }
}
