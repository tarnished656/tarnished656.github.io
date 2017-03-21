package com.lujianbo.app.httpproxy.core.handler;

import com.google.common.net.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URISyntaxException;

public class ProxyConnectHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        if (request.method().equals(HttpMethod.CONNECT)){
            ctx.pipeline().remove(ProxyConnectHandler.this);//移除
            processHttps(ctx,request);
        }else {
            //处理 http的请求
            ctx.pipeline().remove(ProxyConnectHandler.this);//移除
            processHttp(ctx,request);
        }
    }

    private void processHttp(ChannelHandlerContext ctx, HttpRequest request) {
        Bootstrap b = new Bootstrap();
        b.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        //从目标服务器发来的信息都做中继处理
                        ch.pipeline().addLast(new RelayHandler(ctx.channel()));
                    }
                });
        HostAndPort parsedHostAndPort = HostAndPort.fromString(ProxyUtil.parseHostAndPort(request.uri()));
        b.connect(parsedHostAndPort.getHost(),parsedHostAndPort.getPortOrDefault(80))
                .addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                    ctx.pipeline().remove(HttpResponseEncoder.class);
                }
                ctx.pipeline().addLast(new HTTPProxyHandler(future.channel()));
                //成功后将会写入之前的请求
                request.setUri(ProxyUtil.stripHost(request.uri()));
                future.channel().writeAndFlush(request);
            } else {
                ProxyUtil.closeOnFlush(ctx.channel());
            }
        });
    }

    private void processHttps(ChannelHandlerContext ctx, HttpRequest request) throws URISyntaxException {
        Bootstrap b = new Bootstrap();
        b.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RelayHandler(ctx.channel()));
                    }
                });
        /*
         * 连接目标服务器,从request中获取
         * */
        HostAndPort parsedHostAndPort = HostAndPort.fromString(ProxyUtil.parseHostAndPort(request.uri()));
        b.connect(parsedHostAndPort.getHost(),parsedHostAndPort.getPortOrDefault(443))
                .addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                ctx.channel().writeAndFlush(httpsSuccessResponse).addListener(future2 -> {
                    clearHttpHandler(ctx);
                    ctx.pipeline().addLast(new RelayHandler(future.channel()));
                });
            } else {
                ProxyUtil.closeOnFlush(ctx.channel());
            }
        });
    }

    private void clearHttpHandler(ChannelHandlerContext ctx){
        if (ctx.pipeline().get(HttpRequestDecoder.class)!=null){
            ctx.pipeline().remove(HttpRequestDecoder.class);
        }
        if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
            ctx.pipeline().remove(HttpResponseEncoder.class);
        }
        if (ctx.pipeline().get(HttpObjectAggregator.class)!=null){
            ctx.pipeline().remove(HttpObjectAggregator.class);
        }
    }

    private static final HttpResponse httpsSuccessResponse;
    static {
        HttpResponseStatus CONNECTION_ESTABLISHED = new HttpResponseStatus(200, "HTTP/1.1 200 Connection established");
        httpsSuccessResponse=new DefaultHttpResponse(HttpVersion.HTTP_1_1, CONNECTION_ESTABLISHED);
        httpsSuccessResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        httpsSuccessResponse.headers().set("Proxy-Connection", HttpHeaderValues.KEEP_ALIVE);
    }

}
