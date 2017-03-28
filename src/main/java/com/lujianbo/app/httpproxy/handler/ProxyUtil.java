package com.lujianbo.app.httpproxy.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by jianbo on 2016/3/22.
 */
public class ProxyUtil {

    private static Pattern HTTP_PREFIX = Pattern.compile("^https?://.*",
            Pattern.CASE_INSENSITIVE);

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    public static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static String parseHostAndPort(final String uri) {
        final String tempUri;
        if (!HTTP_PREFIX.matcher(uri).matches()) {
            // Browsers particularly seem to send requests in this form when
            // they use CONNECT.
            tempUri = uri;
        } else {
            // We can't just take a substring from a hard-coded index because it
            // could be either http or https.
            tempUri = StringUtils.substringAfter(uri, "://");
        }
        final String hostAndPort;
        if (tempUri.contains("/")) {
            hostAndPort = tempUri.substring(0, tempUri.indexOf("/"));
        } else {
            hostAndPort = tempUri;
        }
        return hostAndPort;
    }

    /**
     * Strips the host from a URI string. This will turn "http://host.com/path"
     * into "/path".
     *
     * @param uri The URI to transform.
     * @return A string with the URI stripped.
     */
    public static String stripHost(final String uri) {
        if (!HTTP_PREFIX.matcher(uri).matches()) {
            // It's likely a URI path, not the full URI (i.e. the host is
            // already stripped).
            return uri;
        }
        final String noHttpUri = StringUtils.substringAfter(uri, "://");
        final int slashIndex = noHttpUri.indexOf("/");
        if (slashIndex == -1) {
            return "/";
        }
        return noHttpUri.substring(slashIndex);
    }
}
