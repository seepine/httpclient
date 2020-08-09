package com.seepine.http.proxy;

import org.apache.http.client.protocol.HttpClientContext;

/**
 * @author Seepine
 */
public interface ProxyProvider {

    /**
     * Get a proxy for task by some strategy.
     *
     * @return proxy
     */
    Proxy getProxy();
}
