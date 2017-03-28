package com.lujianbo.app.httpproxy.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpProxyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast("HttpRequestDecoder", new HttpRequestDecoder());
        ch.pipeline().addLast("HttpResponseEncoder", new HttpResponseEncoder());
        ch.pipeline().addLast(new HttpConnectPooledHandler());
    }
}
