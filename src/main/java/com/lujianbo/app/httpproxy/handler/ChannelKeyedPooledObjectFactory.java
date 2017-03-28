package com.lujianbo.app.httpproxy.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ChannelKeyedPooledObjectFactory implements KeyedPooledObjectFactory<String, Channel> {

    /**
     * is called whenever a new instance is needed.
     */
    @Override
    public PooledObject<Channel> makeObject(String key) throws Exception {
        String host = StringUtils.substringBefore(key, ":");
        int port = Integer.parseInt(StringUtils.substringAfter(key, ":"));
        Channel channel = ConnectorBuilder.getInstance().connect(host, port, () -> new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("HttpRequestEncoder", new HttpRequestEncoder());
                ch.pipeline().addLast("HttpResponseDecoder", new HttpResponseDecoder());
            }
        });
        return new DefaultPooledObject<>(channel);
    }

    /**
     * is invoked on every instance when it is being "dropped" from the pool
     */
    @Override
    public void destroyObject(String key, PooledObject<Channel> p) throws Exception {
        if (p.getObject().isActive()) {
            p.getObject().close();
        }
    }

    /**
     * may be invoked on activated instances to make sure they can be borrowed from the pool. validateObject may also be used to test an instance being returned to the pool before it is passivated. It will only be invoked on an activated instance.
     */
    @Override
    public boolean validateObject(String key, PooledObject<Channel> p) {
        Channel channel = p.getObject();
        return channel.isActive();
    }

    /**
     * is invoked on every instance that has been passivated before it is borrowed from the pool.
     */
    @Override
    public void activateObject(String key, PooledObject<Channel> p) throws Exception {
        //do nothing
    }

    /**
     * is invoked on every instance when it is returned to the pool.
     */
    @Override
    public void passivateObject(String key, PooledObject<Channel> p) throws Exception {
        //clear buff
        p.getObject().flush();
    }
}
