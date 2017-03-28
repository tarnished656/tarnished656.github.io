package com.lujianbo.app.httpproxy.handler;

import com.google.common.net.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * Created by jianbo on 2017/3/28.
 */
public class HttpConnectPooledHandler extends ChannelInboundHandlerAdapter {

    private static ChannelObjectPool channelObjectPool=new ChannelObjectPool();

    private static EventLoopGroup executors = new NioEventLoopGroup(64);

    private Channel outboundChannel;

    private String key="";

    private HttpRelayClientHandler httpRelayClientHandler;

    private boolean isHttps=false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        httpRelayClientHandler=new HttpRelayClientHandler(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isHttps){
            if (outboundChannel!=null&&outboundChannel.isActive()){
                outboundChannel.writeAndFlush(msg);
            }else {
                ctx.close();
            }
        }else {
            if (msg instanceof HttpRequest){
                HttpRequest request=(HttpRequest)msg;
                HostAndPort hostAndPort=HostAndPort.fromString(ProxyUtil.parseHostAndPort(request.uri()));
                boolean https=request.method().equals(HttpMethod.CONNECT);
                if (https){
                    this.isHttps=true;
                    String host=hostAndPort.getHost();
                    int port=hostAndPort.getPortOrDefault(443);
                    //触发该事件的时候 outboundChannel 只能为空
                    if (outboundChannel==null){
                        Channel channel=connect(host,port);
                        //中继流量
                        channel.pipeline().addLast(new RelayHandler(ctx.channel()));
                        this.outboundChannel=channel;
                        //返回事件
                        ctx.channel().writeAndFlush(httpsSuccessResponse).sync();
                        //清理handler
                        clearHttpHandler(ctx);
                    }
                    //doHttps(ctx,host,port);
                }else {
                    String host=hostAndPort.getHost();
                    int port=hostAndPort.getPortOrDefault(80);
                    key=host+":"+port;
                    this.outboundChannel=channelObjectPool.getChannel(key);
                    httpRelayClientHandler.setKey(key);
                    this.outboundChannel.pipeline().addLast(httpRelayClientHandler);
                    this.outboundChannel.writeAndFlush(stripHost(request));
                }
            }else {
                if (this.outboundChannel==null){

                }else {
                    outboundChannel.writeAndFlush(msg);
                }
            }
        }
    }

    private HttpRequest stripHost(HttpRequest request){
        request.setUri(ProxyUtil.stripHost(request.uri()));
        return request;
    }



    private Channel connect(String host,int port) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(executors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                    }
                });
        return  b.connect(host, port).sync().channel();
    }


    @Sharable
    private class HttpRelayClientHandler extends ChannelInboundHandlerAdapter{

        private Channel relayChannel;

        private String key;

        public HttpRelayClientHandler(Channel relayChannel){
            this.relayChannel=relayChannel;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            relayChannel.writeAndFlush(msg);
            if (msg instanceof LastHttpContent){
                outboundChannel=null;
                ctx.pipeline().remove(HttpRelayClientHandler.this);
                channelObjectPool.returnChannel(this.key,ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        }
    }

    private static final HttpResponse httpsSuccessResponse;
    static {
        HttpResponseStatus CONNECTION_ESTABLISHED = new HttpResponseStatus(200, "HTTP/1.1 200 Connection established");
        httpsSuccessResponse=new DefaultHttpResponse(HttpVersion.HTTP_1_1, CONNECTION_ESTABLISHED);
        httpsSuccessResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        httpsSuccessResponse.headers().set("Proxy-Connection", HttpHeaderValues.KEEP_ALIVE);
    }

    private void clearHttpHandler(ChannelHandlerContext ctx){
        if (ctx.pipeline().get(HttpRequestDecoder.class)!=null){
            ctx.pipeline().remove(HttpRequestDecoder.class);
        }
        if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
            ctx.pipeline().remove(HttpResponseEncoder.class);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (!ctx.channel().isActive()){
            ctx.close();
        }
    }
}
