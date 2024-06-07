package com.crawler.challenge.routes;

import com.crawler.challenge.handlers.CrawlHandler;
import com.crawler.challenge.routes.filters.CrawlFilter;

import static spark.Spark.*;

public class CrawlRoute extends AbstractRoute {

    private final CrawlHandler handler;

    public CrawlRoute(CrawlHandler handler) {
        this.handler = handler;
    }

    @Override
    void filters() {
        CrawlFilter.apply();
    }

    @Override
    void routes() {
        path("/crawl", () -> {
            post("", handler::create, getTransformer());
            get("/:id", handler::getById, getTransformer());
        });
    }

}
