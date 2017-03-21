package com.lujianbo.app.httpproxy.core.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ProxyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //用于处理第一个request请求
        ch.pipeline().addLast(new HttpRequestDecoder());
        //用于处理第一个response的回包(仅当Https的时候会使用到)
        ch.pipeline().addLast(new HttpResponseEncoder());
        //用来集中处理request的content部分
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(1048576));

        ch.pipeline().addLast(new ProxyConnectHandler());
    }
}
