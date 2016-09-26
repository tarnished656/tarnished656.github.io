package io.github.lujianbo.httpproxy.core.handler;

import io.github.lujianbo.httpproxy.core.util.ProxyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class HttpsProxyConnectHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final Bootstrap b = new Bootstrap();

    private Channel outboundChannel;

    public HttpsProxyConnectHandler(){

    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final HttpRequest request) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ProxyUtil.closeOnFlush(ctx.channel());
    }


    private void clean(final ChannelHandlerContext ctx){
        try{
            if (ctx.pipeline().get(HttpRequestDecoder.class)!=null){
                ctx.pipeline().remove(HttpRequestDecoder.class);
            }
            if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                ctx.pipeline().remove(HttpResponseEncoder.class);
            }
        }catch (Exception ignored){}
    }

    public void processFailed(final ChannelHandlerContext ctx, final Channel outboundChannel){

    }

    public void connectFailed(final ChannelHandlerContext ctx, final HttpRequest request){

    }

    public HttpResponse respondCONNECTSuccessful(){
        HttpResponse response=new DefaultHttpResponse(HttpVersion.HTTP_1_1, CONNECTION_ESTABLISHED);
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        response.headers().set("Proxy-Connection", HttpHeaders.Values.KEEP_ALIVE);
        return response;
    }

    private static final HttpResponseStatus CONNECTION_ESTABLISHED = new HttpResponseStatus(
            200, "HTTP/1.1 200 Connection established");
}
