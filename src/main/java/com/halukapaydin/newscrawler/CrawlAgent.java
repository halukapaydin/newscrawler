package com.halukapaydin.newscrawler;

import com.halukapaydin.newscrawler.common.Link;
import com.halukapaydin.newscrawler.common.Page;
import com.halukapaydin.newscrawler.linkextrator.LinkExtractor;
import com.halukapaydin.newscrawler.repository.PageRepository;
import com.halukapaydin.newscrawler.request.HttpRequest;
import com.halukapaydin.newscrawler.request.HttpRequestException;
import com.halukapaydin.newscrawler.request.HttpRequestExecutor;
import com.halukapaydin.newscrawler.request.HttpResponse;
import com.halukapaydin.newscrawler.robotstxt.Matcher;
import com.halukapaydin.newscrawler.robotstxt.RobotsParseHandler;
import com.halukapaydin.newscrawler.robotstxt.RobotsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrawlAgent {

    private final HttpRequestExecutor httpRequestExecutor;
    private final PageRepository pageRepository;
    private final LinkExtractor linkExtractor;
    private final CrawlOptions options;
    private final Logger logger = LoggerFactory.getLogger(CrawlAgent.class);
    private final Matcher robotsTxtMatcher;

    public CrawlAgent(HttpRequestExecutor httpRequestExecutor, PageRepository pageRepository, LinkExtractor linkExtractor, CrawlOptions options) {
        this.httpRequestExecutor = httpRequestExecutor;
        this.pageRepository = pageRepository;
        this.linkExtractor = linkExtractor;
        this.options = options;
        this.robotsTxtMatcher = createRobotsMatcher();
    }

    private Matcher createRobotsMatcher() {
        if(!options.isEnableRobotsTxt()){
            return null;
        }
        try{
            RobotsParser robotsParser = new RobotsParser(new RobotsParseHandler());
            String url = options.getUrl();

            URL u = new URL(url);
            String robotsTxtUrl = u.getProtocol() + "://" + u.getHost() + "/robots.txt";
            HttpResponse response = httpRequestExecutor.get(new HttpRequest(robotsTxtUrl));
            Matcher matcher = robotsParser.parse(response.getHtml().getBytes(StandardCharsets.UTF_8));
            return matcher;
        }catch (Exception e){
            logger.info("robots.txt ignored for url {}", options.getUrl());
        }
        return null;
    }

    public void run() {
        Link link = pageRepository.poll();
        if(link == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        logger.debug("link poll. link: '{}'", link);
        HttpResponse response = null;
        try {
            response = httpRequestExecutor.get(new HttpRequest(link.getUrl()));
        } catch (HttpRequestException e) {
            pageRepository.processed(link);
            if(!options.isIgnoreRequestException()){
                throw new RuntimeException(e);
            }
            logger.debug("url ignored. {}", link.getUrl());
            return;
        }
        logger.debug("request finished, statusCode: {}", response.getStatusCode());
        String html = response.getHtml();
        Page page = Page.build(link.getUrl(), link.getDepth(), html, link.getParent() != null ? link.getParent().getUrl() : "", response.getDuration());
        logger.debug("page build for {}", link);

        if (options.getConsumer() != null) {
            logger.debug("page consumed for {}", page);
            options.getConsumer().accept(page);
        }
        if (link.getDepth() < options.getCrawlDepth()) {
            int depth = link.getDepth() + 1;
            Set<String> links = linkExtractor.extract(html, link.getUrl(), options);
            logger.debug("links extracted, count: {}", links.size());

            Stream<String> stream = links.stream();

            if(robotsTxtMatcher != null){
                stream = stream.filter(u->{
                    return robotsTxtMatcher.singleAgentAllowedByRobots(options.getUserAgent(), u);
                });
            }

            if(options.getUrlFilter() != null){
                stream = stream.filter(options.getUrlFilter());
            }

            List<Link> collect = stream.map(l -> new Link(l, depth, link)).collect(Collectors.toList());
            pageRepository.addAll(collect);
            logger.debug("links added, count: {}", links.size());
        }
        pageRepository.processed(link);
        logger.debug("finished, link {}", link);
    }


}
