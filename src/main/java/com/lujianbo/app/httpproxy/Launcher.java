package com.lujianbo.app.httpproxy;

import com.lujianbo.app.httpproxy.core.ProxyServer;
import com.lujianbo.app.httpproxy.core.handler.ProxyServerInitializer;

/**
 * Created by jianbo on 2016/3/22.
 */
public class Launcher {

    public static void main(String[] args) {
        ProxyServer proxyServer=new ProxyServer(new ProxyServerInitializer());
        proxyServer.start();
    }
}
