package com.halukapaydin.newscrawler.linkextrator;

import com.halukapaydin.newscrawler.CrawlOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class LinkExtractorImpl implements LinkExtractor {

    @Override
    public Set<String> extract(String html, String urlString, CrawlOptions options) {
        URL url = toURL(urlString);
        if(url == null){
            return null;
        }

        String rootLink = getRootParent(url);
        String parentLink = getLinkParent(url);

        Document document = Jsoup.parse(html, urlString);
        Elements elements = document.select("a");
        Set<String> links = new HashSet<>();
        for (Element element : elements) {
            String href = element.attr("href");
            String link = "";
            if(href.matches("^(?i)(http|https)://.*$")){
                link = href;
            }else{
                if(href.contains(":")){
                    continue;
                }
                href = URLEncoder.encode(href);
                if(href.startsWith("/")){
                    link = rootLink + href;
                }else {
                    link = parentLink + href;
                }
            }
            if(!options.isSameDomain() || isLinkInDomain(link, options)){
                links.add(link);
            }
        }
        return links;
    }

    private boolean isLinkInDomain(String link, CrawlOptions options){
        URL linkUrl = toURL(link);
        if(linkUrl != null){
            return linkUrl.getHost().contains(options.getTopLevelDomain());
        }
        return false;
    }

    private URL toURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private String getLinkParent(URL url){
        String path = url.getPath();
        int index = path.lastIndexOf("/");
        if(index > 0){
            path = path.substring(0, index);
        }
        return url.getProtocol() + "://" + url.getHost() + path;
    }

    private String getRootParent(URL url){
        return url.getProtocol() + "://" + url.getHost();
    }

}
