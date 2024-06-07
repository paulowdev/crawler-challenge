package com.crawler.challenge.dtos;

public class CreateCrawlRequest {

    private String keyword;

    public CreateCrawlRequest(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "CreateCrawlRequest{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}
