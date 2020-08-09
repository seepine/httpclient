package com.seepine.http.proxy;

import java.util.Objects;

/**
 * @author Seepine
 */
public class Proxy {

    private final String host;
    private final int port;
    private String username;
    private String password;

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Proxy(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Proxy proxy = (Proxy) o;

        if (port != proxy.port) {
            return false;
        }
        if (!Objects.equals(host, proxy.host)) {
            return false;
        }
        if (!Objects.equals(username, proxy.username)) {
            return false;
        }
        return Objects.equals(password, proxy.password);
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}