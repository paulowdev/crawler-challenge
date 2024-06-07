package com.crawler.challenge.dtos;

public class CreateCrawlResponse {

    private String id;

    public CreateCrawlResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CreateCrawlResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
