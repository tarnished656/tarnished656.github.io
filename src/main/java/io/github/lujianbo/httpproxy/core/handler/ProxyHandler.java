package io.github.lujianbo.httpproxy.core.handler;

import com.google.common.net.HostAndPort;
import io.github.lujianbo.httpproxy.core.util.ProxyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class ProxyHandler extends SimpleChannelInboundHandler<Object> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final Bootstrap b = new Bootstrap();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object object) throws Exception {
        if (object instanceof HttpRequest){
            HttpRequest request=(HttpRequest)object;
            if (request.getMethod().equals(HttpMethod.CONNECT)){
                ctx.pipeline().remove(ProxyHandler.this);//移除
                processHttps(ctx,request);
            }else {
                ctx.pipeline().remove(ProxyHandler.this);//移除
                processHttp(ctx,request);
            }
        }else {

        }
    }

    private void processHttp(ChannelHandlerContext ctx, HttpRequest request) {
        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        //ch.pipeline().addLast(new HttpObjectAggregator(1048576));
                        ch.pipeline().addLast(new ProxyToServerHandler(ctx.channel()));

                        request.setUri(ProxyUtil.stripHost(request.getUri()));
                        ch.writeAndFlush(request);
                    }
                });
        HostAndPort parsedHostAndPort = HostAndPort.fromString(ProxyUtil.parseHostAndPort(request.getUri()));
        b.connect(parsedHostAndPort.getHostText(),parsedHostAndPort.getPortOrDefault(80)).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                    ctx.pipeline().remove(HttpResponseEncoder.class);
                }
                ctx.pipeline().addLast(new ClientToProxyHandler(future.channel()));
            } else {
                ProxyUtil.closeOnFlush(ctx.channel());
            }
        });
    }

    private void processHttps(ChannelHandlerContext ctx, HttpRequest request) throws URISyntaxException {
        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RelayHandler(ctx.channel()));
                    }
                });//把promise设置到Handler中来触发promise
        /**
         * 连接目标服务器,从request中获取
         * */
        HostAndPort parsedHostAndPort = HostAndPort.fromString(ProxyUtil.parseHostAndPort(request.getUri()));

        b.connect(parsedHostAndPort.getHostText(),parsedHostAndPort.getPortOrDefault(443)).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                ctx.channel().writeAndFlush(respondCONNECTSuccessful()).addListener(future2 -> {
                    if (ctx.pipeline().get(HttpRequestDecoder.class)!=null){
                        ctx.pipeline().remove(HttpRequestDecoder.class);
                    }
                    if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                        ctx.pipeline().remove(HttpResponseEncoder.class);
                    }
                    if (ctx.pipeline().get(HttpObjectAggregator.class)!=null){
                        ctx.pipeline().remove(HttpObjectAggregator.class);
                    }
                    ctx.pipeline().addLast(new RelayHandler(future.channel()));
                });
            } else {
                ProxyUtil.closeOnFlush(ctx.channel());
            }
        });
    }

    private HttpResponse respondCONNECTSuccessful(){
        HttpResponse response=new DefaultHttpResponse(HttpVersion.HTTP_1_1, CONNECTION_ESTABLISHED);
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        response.headers().set("Proxy-Connection", HttpHeaders.Values.KEEP_ALIVE);
        return response;
    }

    private static final HttpResponseStatus CONNECTION_ESTABLISHED = new HttpResponseStatus(
            200, "HTTP/1.1 200 Connection established");



}
