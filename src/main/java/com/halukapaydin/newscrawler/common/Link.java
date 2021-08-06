package com.halukapaydin.newscrawler.common;

import java.util.Objects;

public class Link {

    private Link parent;
    private String url;
    private int depth;

    public Link(String url, int depth, Link parent) {
        this.url = url;
        this.depth = depth;
        this.parent = parent;
    }

    public Link getParent() {
        return parent;
    }

    public void setParent(Link parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "(" + depth + ") " + url;
    }
}
