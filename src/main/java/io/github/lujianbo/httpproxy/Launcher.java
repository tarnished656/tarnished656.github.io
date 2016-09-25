package io.github.lujianbo.httpproxy;

import io.github.lujianbo.httpproxy.core.ProxyServer;
import io.github.lujianbo.httpproxy.core.handler.HttpProxyServerInitializer;

/**
 * Created by jianbo on 2016/3/22.
 */
public class Launcher {



    public static void main(String[] args) {

        ProxyServer proxyServer=new ProxyServer(new HttpProxyServerInitializer());
        proxyServer.start();
    }


}
