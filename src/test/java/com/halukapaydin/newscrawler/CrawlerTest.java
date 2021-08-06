package com.halukapaydin.newscrawler;

import com.halukapaydin.newscrawler.request.HtmlUnitHttpRequestExecutorImpl;
import com.halukapaydin.newscrawler.robotstxt.Matcher;
import com.halukapaydin.newscrawler.robotstxt.RobotsParseHandler;
import com.halukapaydin.newscrawler.robotstxt.RobotsParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class CrawlerTest {

    @Test
    public void test() throws InterruptedException {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        CrawlOptions options = new CrawlOptions();
        options.setCrawlDepth(3);
        options.setThreadCount(30);
        options.setIgnoreRequestException(true);
//        options.setUrl("https://www.aydinlik.com.tr");
        options.setUrl("https://www.mynet.com");
        AtomicInteger count = new AtomicInteger(0);
        options.setConsumer(page -> {
            String html = page.getHtml();
            String substring = html.substring(0, Math.min(html.length(), 100));
//            substring = "";
            Document document = Jsoup.parse(html);
            substring = document.select("title").text();
            System.err.println(count.incrementAndGet() +  " - (" + page.getDepth() + " " + page.getLoadDuration() + "ms" + ") " + page.getUrl());
        });


        Crawler crawler = new Crawler();
//        crawler.setHttpRequestExecutorFactory(HtmlUnitHttpRequestExecutorImpl::new);
        crawler.setOptions(options);
        crawler.start(true);

    }

}