package com.halukapaydin.newscrawler;

import com.halukapaydin.newscrawler.request.HtmlUnitHttpRequestExecutorImpl;
import com.halukapaydin.newscrawler.request.HttpRequest;
import com.halukapaydin.newscrawler.request.HttpRequestException;
import com.halukapaydin.newscrawler.request.HttpResponse;
import org.junit.jupiter.api.Test;

class HtmlUnitHttpRequestExecutorImplTest {

    @Test
    public void test() throws HttpRequestException {
        HtmlUnitHttpRequestExecutorImpl executor = new HtmlUnitHttpRequestExecutorImpl();
        HttpResponse response = executor.get(new HttpRequest("https://www.aydinlik.com.tr/haber/orman-iscisinden-ogluna-helikopterli-amcalariniz-arkadaslarim-252775"));
        System.err.println(response.getHtml());
    }


}