package com.ldbbank.autodebit_svc._framwork;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.time.Duration;

@Component
public class CustomRestTemplateBuilder {
    private final RestTemplateBuilder restTemplateBuilder;
    public CustomRestTemplateBuilder(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }
    public RestTemplate build(int connectionTimeout, int readTimeout){
        try{
            TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            RestTemplate rest = restTemplateBuilder
                    .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                    .setReadTimeout(Duration.ofMillis(readTimeout))
                    .build();
            rest.setRequestFactory(requestFactory);
            return rest;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}