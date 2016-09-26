package io.github.lujianbo.httpproxy.core.handler;

import io.github.lujianbo.httpproxy.core.util.ProxyUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by jianbo on 2016/9/26.
 */
public class ProxyToServerHandler extends ChannelInboundHandlerAdapter {

    private Channel clientToProxy;

    public ProxyToServerHandler(Channel clientToProxy){
        this.clientToProxy=clientToProxy;
    }

    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (clientToProxy.isActive()) {
            clientToProxy.writeAndFlush(msg);
        }else {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (clientToProxy.isActive()) {
            ProxyUtil.closeOnFlush(clientToProxy);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        clientToProxy.close();
    }
}
