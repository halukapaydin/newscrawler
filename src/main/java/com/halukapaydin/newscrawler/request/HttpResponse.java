package com.halukapaydin.newscrawler.request;

public class HttpResponse {

    private String html;
    private int statusCode;
    private long duration;

    public HttpResponse(String html, int statusCode, long duration) {
        this.html = html;
        this.statusCode = statusCode;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
