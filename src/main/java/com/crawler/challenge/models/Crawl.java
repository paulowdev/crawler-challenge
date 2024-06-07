package com.crawler.challenge.models;

import java.util.HashSet;
import java.util.Set;

public class Crawl {

    private String id;
    private CrawlStatus status;
    private Set<String> urls = new HashSet<>();

    public Crawl(String id, CrawlStatus status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public CrawlStatus getStatus() {
        return status;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(CrawlStatus status) {
        this.status = status;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Crawl{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", urls=" + urls +
                '}';
    }
}
