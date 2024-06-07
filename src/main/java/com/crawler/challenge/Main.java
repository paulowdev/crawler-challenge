package com.crawler.challenge;

import com.crawler.challenge.handlers.CrawlHandler;
import com.crawler.challenge.routes.CrawlRoute;

public class Main {
    public static void main(String[] args) {
        new CrawlRoute(new CrawlHandler()).use();
    }
}
