package com.seepine.http.proxy;

import com.seepine.http.entity.Request;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Seepine
 */
public class SimpleProxyProvider implements ProxyProvider {

    private final List<Proxy> proxies;

    private final AtomicInteger pointer;

    public SimpleProxyProvider(List<Proxy> proxies) {
        this(proxies, new AtomicInteger(-1));
    }

    private SimpleProxyProvider(List<Proxy> proxies, AtomicInteger pointer) {
        this.proxies = proxies;
        this.pointer = pointer;
    }

    public static SimpleProxyProvider from(Proxy... proxies) {
        return from(Arrays.asList(proxies));
    }

    public static SimpleProxyProvider from(List<Proxy> proxies) {
        return new SimpleProxyProvider(Collections.unmodifiableList(proxies));
    }


    @Override
    public Proxy getProxy() {
        if (proxies == null || proxies.isEmpty()) {
            return null;
        }
        return proxies.get(incrForLoop());
    }

    private int incrForLoop() {
        int p = pointer.incrementAndGet();
        int size = proxies.size();
        if (p < size) {
            return p;
        }
        while (!pointer.compareAndSet(p, p % size)) {
            p = pointer.get();
        }
        return p % size;
    }

}