package com.halukapaydin.newscrawler.request;

public class HttpRequestException extends Exception {

    private String url;

    public HttpRequestException(String url, Throwable cause) {
        super(String.format("Unable to fetch '%s'", url), cause);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
