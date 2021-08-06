package com.halukapaydin.newscrawler.repository;

import com.halukapaydin.newscrawler.common.Link;

import java.util.*;
import java.util.concurrent.*;

public class PageRepositoryImpl implements PageRepository {

    private Set<String> visitedLinks = new HashSet<>();
    private LinkedBlockingQueue<Link> queue = new LinkedBlockingQueue<>();
    private List<Link> locks = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Link poll() {
        Link link = queue.poll();
        if(link != null) {
            locks.add(link);
        }
        return link;
    }

    @Override
    public void addAll(Collection<Link> links) {
        for (Link link : links) {
            add(link);
        }
    }

    private synchronized void add(Link link){
        if(visitedLinks.contains(link.getUrl())){
            return;
        }
        visitedLinks.add(link.getUrl());
        queue.add(link);
    }

    @Override
    public void processed(Link link) {
        locks.remove(link);
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty() && locks.isEmpty();
    }


}
