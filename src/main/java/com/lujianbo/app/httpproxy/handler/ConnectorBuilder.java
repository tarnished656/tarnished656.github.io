package com.lujianbo.app.httpproxy.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ConnectorBuilder {

    private static EventLoopGroup executors = new NioEventLoopGroup();

    private ConnectorBuilder() {
        //lots of initialization code
    }

    public static ConnectorBuilder getInstance() {
        return ConnectorBuilderHolder.instance;
    }

    public Channel connect(String host, int port, Supplier<ChannelHandler> initSupplier) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(executors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(initSupplier.get());
        return b.connect(host, port).sync().channel();
    }

    private static class ConnectorBuilderHolder {
        public static final ConnectorBuilder instance = new ConnectorBuilder();
    }
}
