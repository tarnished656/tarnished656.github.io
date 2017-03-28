package com.lujianbo.app.httpproxy.handler;

import io.netty.channel.Channel;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ChannelObjectPool {

    private KeyedObjectPool<String,Channel> channelKeyedObjectPool;

    public ChannelObjectPool(GenericKeyedObjectPoolConfig config){
        this.channelKeyedObjectPool= new GenericKeyedObjectPool<>(new ChannelKeyedPooledObjectFactory(),config);
    }

    public ChannelObjectPool(){
        GenericKeyedObjectPoolConfig config=new GenericKeyedObjectPoolConfig();
        config.setMaxTotalPerKey(64);
        config.setMaxTotal(1024*1204);
        config.setMaxIdlePerKey(6);//一个连接最多6个空闲
        this.channelKeyedObjectPool= new GenericKeyedObjectPool<>(new ChannelKeyedPooledObjectFactory(),config);
    }

    public Channel getChannel(String key) throws Exception {
        return channelKeyedObjectPool.borrowObject(key);
    }

    public void returnChannel(String key,Channel channel) throws Exception {
        channelKeyedObjectPool.returnObject(key,channel);
    }

}
