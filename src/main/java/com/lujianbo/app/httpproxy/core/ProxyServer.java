package com.lujianbo.app.httpproxy.core;

import com.lujianbo.app.httpproxy.core.handler.ProxyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by jianbo on 2016/3/22.
 */
public class ProxyServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "7778"));

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ChannelInitializer<SocketChannel> initializer;

    public ProxyServer(ChannelInitializer<SocketChannel> initializer){
        this.initializer=initializer;
    }

    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            b.bind(PORT).sync().addListener(future -> {
                if (future.isSuccess()){
                    System.out.println("启动成功");
                }else {
                    System.out.println("启动失败");
                }
            });
        } catch (Exception e) {
            stop();
        }
    }

    public void stop(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        System.out.println("正在关闭");
    }

    public static void main(String[] args) {
        ProxyServer proxyServer=new ProxyServer(new ProxyServerInitializer());
        proxyServer.start();
    }
}
