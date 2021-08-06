package com.halukapaydin.newscrawler.request;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.halukapaydin.newscrawler.CrawlOptions;

import java.io.IOException;
import java.util.logging.Level;

public class HtmlUnitHttpRequestExecutorImpl implements HttpRequestExecutor {
    private WebClient webClient;

    public HtmlUnitHttpRequestExecutorImpl() {
        this.webClient = new WebClient(BrowserVersion.FIREFOX_78);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        WebClientOptions options = webClient.getOptions();
        options.setThrowExceptionOnScriptError(false);
        options.setPrintContentOnFailingStatusCode(false);
        options.setCssEnabled(false);
        options.setDownloadImages(false);
        options.setJavaScriptEnabled(true);
        options.setWebSocketEnabled(false);
        options.setTimeout(30000);
    }

    @Override
    public HttpResponse get(HttpRequest url) throws HttpRequestException {
        HtmlPage page = null;
        try {
            long startTime = System.currentTimeMillis();
            page = webClient.getPage(url.getUrl());
            WebResponse webResponse = page.getWebResponse();
            return new HttpResponse(page.asXml(), webResponse.getStatusCode(), System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            throw new HttpRequestException(url.getUrl(), e);
        }
    }
}
