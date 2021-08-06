package com.halukapaydin.newscrawler.common;

public class Page {

    private String url;
    private String parentUrl;
    private int depth;
    private String html;
    private long loadDuration;

    public static Page build(String url, int depth, String html, String parentUrl, long loadDuration) {
        Page page = new Page();
        page.setUrl(url);
        page.setHtml(html);
        page.setDepth(depth);
        page.setLoadDuration(loadDuration);
        page.setParentUrl(parentUrl);
        return page;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public long getLoadDuration() {
        return loadDuration;
    }

    public void setLoadDuration(long loadDuration) {
        this.loadDuration = loadDuration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "(" + depth + ") " + url;
    }
}
