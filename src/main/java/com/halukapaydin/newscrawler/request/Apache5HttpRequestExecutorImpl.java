package com.halukapaydin.newscrawler.request;

import com.google.gson.Gson;
import com.halukapaydin.newscrawler.CrawlOptions;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.util.Timeout;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Apache5HttpRequestExecutorImpl implements HttpRequestExecutor {
    private final CloseableHttpClient httpclient;
    private Gson gson = new Gson();

    public Apache5HttpRequestExecutorImpl(CrawlOptions options) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        if(options.getCookies() != null){
            for (String cookieString : options.getCookies()) {
                basicCookieStore.addCookie(gson.fromJson(cookieString, BasicClientCookie.class));
            }
        }
        if(options.getUserAgent() != null){
            builder.setDefaultHeaders(Collections.singleton(new BasicHeader("user-agent", options.getUserAgent())));
        }
        builder.setDefaultCookieStore(basicCookieStore);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.of(20, TimeUnit.SECONDS))
                .build();

        builder.setDefaultRequestConfig(config);
        httpclient = builder.build();
    }

    @Override
    public HttpResponse get(HttpRequest request) throws HttpRequestException {
        HttpGet getRequest = new HttpGet(request.getUrl());
        long startTime = System.currentTimeMillis();
        try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
            return new HttpResponse(EntityUtils.toString(response.getEntity()), response.getCode(), (int) (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            throw new HttpRequestException(request.getUrl(), e);
        }
    }
}
