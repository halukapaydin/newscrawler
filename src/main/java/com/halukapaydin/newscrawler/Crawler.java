package com.halukapaydin.newscrawler;

import com.halukapaydin.newscrawler.common.Link;
import com.halukapaydin.newscrawler.linkextrator.LinkExtractor;
import com.halukapaydin.newscrawler.linkextrator.LinkExtractorImpl;
import com.halukapaydin.newscrawler.repository.PageRepository;
import com.halukapaydin.newscrawler.repository.PageRepositoryImpl;
import com.halukapaydin.newscrawler.request.Apache5HttpRequestExecutorImpl;
import com.halukapaydin.newscrawler.request.HttpRequestExecutor;

import java.util.Collections;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Crawler {
    private CrawlOptions options;
    private boolean running = false;
    private ExecutorService executorService;
    private Supplier<HttpRequestExecutor>  httpRequestExecutorFactory = ()->{
        return new Apache5HttpRequestExecutorImpl(options);
    };
    private PageRepository pageRepository = new PageRepositoryImpl();
    private LinkExtractor linkExtractor = new LinkExtractorImpl();

    public Supplier<HttpRequestExecutor> getHttpRequestExecutorFactory() {
        return httpRequestExecutorFactory;
    }

    public void setHttpRequestExecutorFactory(Supplier<HttpRequestExecutor> httpRequestExecutorFactory) {
        this.httpRequestExecutorFactory = httpRequestExecutorFactory;
    }

    public PageRepository getPageRepository() {
        return pageRepository;
    }

    public void setPageRepository(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public LinkExtractor getLinkExtractor() {
        return linkExtractor;
    }

    public void setLinkExtractor(LinkExtractor linkExtractor) {
        this.linkExtractor = linkExtractor;
    }

    public CrawlOptions getOptions() {
        return options;
    }

    public void setOptions(CrawlOptions options) {
        this.options = options;
    }

    public void start(boolean waitForFinish) {
        running = true;
        executorService = Executors.newFixedThreadPool(options.getThreadCount());
        pageRepository.addAll(Collections.singleton(new Link(options.getUrl(), 0,null)));
        CompletableFuture[] completableFutures = new CompletableFuture[options.getThreadCount()];
        for (int i = 0; i < options.getThreadCount(); i++) {
            completableFutures[i] = CompletableFuture.runAsync(() -> {
                HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.get();
                CrawlAgent agent = new CrawlAgent(httpRequestExecutor, pageRepository, linkExtractor, options);
                while (running && isAllLinksDownloaded()) {
                    agent.run();
                }
            }, executorService);
        }
        if(waitForFinish){
            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutures);
            try {
                allOf.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private boolean isAllLinksDownloaded() {
        return !pageRepository.isEmpty();
    }


    public void stop(){
        running = false;
        executorService.shutdown();
    }

}
