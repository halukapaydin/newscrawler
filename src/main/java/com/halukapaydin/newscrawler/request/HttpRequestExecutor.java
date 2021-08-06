package com.halukapaydin.newscrawler.request;

public interface HttpRequestExecutor {

    HttpResponse get(HttpRequest url) throws HttpRequestException;


}
