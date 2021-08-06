package com.halukapaydin.newscrawler.robotstxt;

import com.halukapaydin.newscrawler.CrawlOptions;
import com.halukapaydin.newscrawler.request.Apache5HttpRequestExecutorImpl;
import com.halukapaydin.newscrawler.request.HttpRequest;
import com.halukapaydin.newscrawler.request.HttpRequestException;
import com.halukapaydin.newscrawler.request.HttpResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RobotsParserTest {

    @Test
    void parse() throws HttpRequestException {
        Apache5HttpRequestExecutorImpl requestExecutor = new Apache5HttpRequestExecutorImpl(new CrawlOptions());
        HttpResponse response = requestExecutor.get(new HttpRequest("http://www.mynet.com/robots.txt"));
        RobotsParser parser = new RobotsParser(new RobotsParseHandler());
        Matcher matcher = parser.parse(response.getHtml().getBytes());
        boolean b = matcher.singleAgentAllowedByRobots("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0", "https://www.mynet.com/haber/pv/gorevden-af-talebi-kabul-edilen-ziya-selcuk-tan-ilk-aciklama-110106838987");
        System.err.println(b);


    }
}