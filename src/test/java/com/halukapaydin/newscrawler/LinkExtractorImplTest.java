package com.halukapaydin.newscrawler;

import com.halukapaydin.newscrawler.linkextrator.LinkExtractorImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

class LinkExtractorImplTest {

    @Test
    void extractw() throws IOException {
        String urlString = "https://www.mynet.com/sans-oyunlari/sans-topu-sonuclari?html=asdklj&dasdkjal";
        URL url = new URL(urlString);
        System.err.println(urlString);

    }
    @Test
    void extract() throws IOException {
        LinkExtractorImpl linkExtractor = new LinkExtractorImpl();
        String html = new String(Files.readAllBytes(Paths.get("src/test/resources/htmls/www.mynet.com.html")));
        CrawlOptions options = new CrawlOptions();
        String urlString = "https://www.mynet.com/sans-oyunlari/sans-topu-sonuclari?html=asdklj&dasdkjal";
        options.setUrl("https://www.mynet.com");
        Set<String> extract = linkExtractor.extract(html, urlString, options);
        for (String s : extract) {
            System.err.println(s);
        }

    }
}