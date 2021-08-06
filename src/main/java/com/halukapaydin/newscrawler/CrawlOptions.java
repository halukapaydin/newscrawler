package com.halukapaydin.newscrawler;

import com.google.common.net.InternetDomainName;
import com.halukapaydin.newscrawler.common.Page;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CrawlOptions {
    private String url;
    private int threadCount = 1;
    private int crawlDepth = 3;
    private boolean sameDomain = true;
    private Consumer<Page> consumer;
    private Predicate<String> urlFilter;
    private String topLevelDomain;
    private boolean ignoreRequestException = true;
    private boolean enableRobotsTxt = true;
    private List<String> cookies = new ArrayList<>();
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String host = u.getHost();
        InternetDomainName name = InternetDomainName.from(host);
        this.topLevelDomain = name.topDomainUnderRegistrySuffix().toString();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isEnableRobotsTxt() {
        return enableRobotsTxt;
    }

    public void setEnableRobotsTxt(boolean enableRobotsTxt) {
        this.enableRobotsTxt = enableRobotsTxt;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(int crawlDepth) {
        this.crawlDepth = crawlDepth;
    }

    public Consumer<Page> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<Page> consumer) {
        this.consumer = consumer;
    }

    public String getTopLevelDomain() {
        return topLevelDomain;
    }

    public boolean isIgnoreRequestException() {
        return ignoreRequestException;
    }

    public void setIgnoreRequestException(boolean ignoreRequestException) {
        this.ignoreRequestException = ignoreRequestException;
    }

    public boolean isSameDomain() {
        return sameDomain;
    }

    public void setSameDomain(boolean sameDomain) {
        this.sameDomain = sameDomain;
    }

    public Predicate<String> getUrlFilter() {
        return urlFilter;
    }

    public void setUrlFilter(Predicate<String> urlFilter) {
        this.urlFilter = urlFilter;
    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }
}
