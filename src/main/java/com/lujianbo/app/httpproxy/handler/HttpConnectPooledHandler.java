package com.lujianbo.app.httpproxy.handler;

import com.google.common.net.HostAndPort;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;


/**
 * Created by jianbo on 2017/3/28.
 */
public class HttpConnectPooledHandler extends ChannelInboundHandlerAdapter {

    private static final HttpResponse httpsSuccessResponse;
    private static ChannelObjectPool channelObjectPool = new ChannelObjectPool();

    static {
        HttpResponseStatus CONNECTION_ESTABLISHED = new HttpResponseStatus(200, "HTTP/1.1 200 Connection established");
        httpsSuccessResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, CONNECTION_ESTABLISHED);
        httpsSuccessResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        httpsSuccessResponse.headers().set("Proxy-Connection", HttpHeaderValues.KEEP_ALIVE);
    }

    private Channel outboundChannel;
    private String key = "";
    private HttpRelayClientHandler httpRelayClientHandler;
    private boolean isHttps = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        httpRelayClientHandler = new HttpRelayClientHandler(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isHttps) {
            if (outboundChannel != null && outboundChannel.isActive()) {
                outboundChannel.writeAndFlush(msg);
            } else {
                ctx.close();
            }
        } else {
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                HostAndPort hostAndPort = HostAndPort.fromString(ProxyUtil.parseHostAndPort(request.uri()));
                boolean https = request.method().equals(HttpMethod.CONNECT);
                if (https) {
                    this.isHttps = true;
                    String host = hostAndPort.getHost();
                    int port = hostAndPort.getPortOrDefault(443);
                    //触发该事件的时候 outboundChannel 只能为空
                    if (outboundChannel == null) {
                        Channel channel = connect(host, port);
                        channel.pipeline().addLast(new RelayHandler(ctx.channel()));
                        this.outboundChannel = channel;
                        ctx.channel().writeAndFlush(httpsSuccessResponse).sync();
                        clearHttpHandler(ctx);
                    }
                } else {
                    String host = hostAndPort.getHost();
                    int port = hostAndPort.getPortOrDefault(80);
                    key = host + ":" + port;
                    this.outboundChannel = channelObjectPool.getChannel(key);
                    httpRelayClientHandler.setKey(key);
                    this.outboundChannel.pipeline().addLast(httpRelayClientHandler);
                    this.outboundChannel.writeAndFlush(stripHost(request));
                }
            } else {
                if (this.outboundChannel == null) {

                } else {
                    outboundChannel.writeAndFlush(msg);
                }
            }
        }
    }

    private HttpRequest stripHost(HttpRequest request) {
        request.setUri(ProxyUtil.stripHost(request.uri()));
        return request;
    }

    private Channel connect(String host, int port) throws InterruptedException {
        Channel channel = ConnectorBuilder.getInstance().connect(host, port, () -> new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
            }
        });
        return channel;
    }

    private void clearHttpHandler(ChannelHandlerContext ctx) {
        if (ctx.pipeline().get(HttpRequestDecoder.class) != null) {
            ctx.pipeline().remove(HttpRequestDecoder.class);
        }
        if (ctx.pipeline().get(HttpResponseEncoder.class) != null) {
            ctx.pipeline().remove(HttpResponseEncoder.class);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!ctx.channel().isActive()) {
            ctx.close();
        }
    }

    @Sharable
    private class HttpRelayClientHandler extends ChannelInboundHandlerAdapter {

        private Channel relayChannel;

        private String key;

        public HttpRelayClientHandler(Channel relayChannel) {
            this.relayChannel = relayChannel;
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
            if (msg instanceof LastHttpContent) {
                outboundChannel = null;
                ctx.pipeline().remove(HttpRelayClientHandler.this);
                channelObjectPool.returnChannel(this.key, ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (!ctx.channel().isActive()){
                channelObjectPool.invalidateChannel(this.key, ctx.channel());
            }
        }
    }
}
