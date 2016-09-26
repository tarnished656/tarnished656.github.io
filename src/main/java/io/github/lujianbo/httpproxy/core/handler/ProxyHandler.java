package io.github.lujianbo.httpproxy.core.handler;

import io.github.lujianbo.httpproxy.core.util.ProxyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Promise;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class ProxyHandler extends SimpleChannelInboundHandler<HttpObject> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final Bootstrap b = new Bootstrap();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject object) throws Exception {
        if (object instanceof HttpRequest){
            HttpRequest request=(HttpRequest)object;
            if (request.getMethod().equals(HttpMethod.CONNECT)){
                ctx.pipeline().remove(ProxyHandler.this);//移除
                processHttps(ctx,request);
            }else {
                if (ctx.pipeline().get(HttpServerCodec.class)!=null){
                    ctx.pipeline().remove(HttpServerCodec.class);
                }
                ctx.pipeline().remove(ProxyHandler.this);//移除
                processHttp(ctx,request);
            }
        }
    }

    private void processHttp(ChannelHandlerContext ctx, HttpRequest request) throws URISyntaxException {
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(future -> {
                    Channel outboundChannel = (Channel) future.getNow();
                    if (future.isSuccess()) {
                        outboundChannel.pipeline().addLast(new HttpRequestEncoder());
                        outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                        ctx.pipeline().fireChannelRead(request);
                    } else {
                        ProxyUtil.closeOnFlush(ctx.channel());
                    }
                });
        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new PromiseHandler(promise));//把promise设置到Handler中来触发promise

        URI uri=new URI(request.getUri());
        b.connect(uri.getHost(), uri.getPort()==-1?80:uri.getPort()).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("连接"+uri.getHost()+":"+uri.getPort()+"成功");
                //清理 HttpResponseEncoder
                if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                    ctx.pipeline().remove(HttpResponseEncoder.class);
                }
                ctx.pipeline().addLast(new HttpProxyConnectHandler(future.channel()));
            } else {
                ProxyUtil.closeOnFlush(ctx.channel());
            }
        });
    }

    private void processHttps(ChannelHandlerContext ctx, HttpRequest request) throws URISyntaxException {
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
                future -> {
                    Channel outboundChannel  = (Channel) future.getNow();
                    if (future.isSuccess()) {
                        ctx.channel().writeAndFlush(respondCONNECTSuccessful()).addListener(future2 -> {
                            //清理handler
                            if (ctx.pipeline().get(HttpRequestDecoder.class)!=null){
                                ctx.pipeline().remove(HttpRequestDecoder.class);
                            }
                            if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                                ctx.pipeline().remove(HttpResponseEncoder.class);
                            }
                            ctx.pipeline().addLast(new RelayHandler(outboundChannel));
                            outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                        });
                    } else {
                        ProxyUtil.closeOnFlush(ctx.channel());
                    }
                });
        /*
         * 配置Handler
         * */
        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new PromiseHandler(promise));//把promise设置到Handler中来触发promise
        /**
         * 连接目标服务器,从request中获取
         * */

        String  host= StringUtils.substringBefore(request.getUri(),":");
        String  port= StringUtils.substringAfter(request.getUri(),":");
        b.connect(host,Integer.valueOf(port)).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("连接远程地址成功");
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
