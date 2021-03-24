package io.quarkiverse.jberet.rest.runtime;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jberet.rest.client.BatchClient;

import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.LaunchMode;

public class JBeretRestProducer {
    @ConfigProperty(name = "quarkus.http.host")
    private String host;
    @ConfigProperty(name = "quarkus.http.port")
    private int port;
    @ConfigProperty(name = "quarkus.http.ssl-port")
    private int sslPort;
    @ConfigProperty(name = "quarkus.http.test-port")
    private int testPort;
    @ConfigProperty(name = "quarkus.http.test-ssl-port")
    private int testSllPort;
    @ConfigProperty(name = "quarkus.http.insecure-requests")
    private String insecure;

    @Produces
    @DefaultBean
    @Singleton
    public BatchClient batchClient() {
        final String scheme = isInsecurityEnabled() ? "http" : "https";
        return new BatchClient(scheme + "://" + host + ":" + resolvePort());
    }

    private int resolvePort() {
        if (isInsecurityEnabled()) {
            if (LaunchMode.current() == LaunchMode.TEST) {
                return testPort;
            } else {
                return port;
            }
        } else {
            if (LaunchMode.current() == LaunchMode.TEST) {
                return testSllPort;
            } else {
                return sslPort;
            }
        }

    }

    private boolean isInsecurityEnabled() {
        return "enabled".equals(insecure);
    }
}
