package com.halukapaydin.newscrawler.linkextrator;

import com.halukapaydin.newscrawler.CrawlOptions;

import java.util.List;
import java.util.Set;

public interface LinkExtractor {

    Set<String> extract(String html, String url, CrawlOptions options);

}
