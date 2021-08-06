package com.halukapaydin.newscrawler.repository;

import com.halukapaydin.newscrawler.common.Link;

import java.util.Collection;

public interface PageRepository {

    Link poll();

    void addAll(Collection<Link> links);

    void processed(Link link);

    boolean isEmpty();
}
