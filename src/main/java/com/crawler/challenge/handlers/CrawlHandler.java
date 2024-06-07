package com.crawler.challenge.handlers;

import com.crawler.challenge.dtos.CreateCrawlResponse;
import com.crawler.challenge.dtos.mappers.CreateCrawlRequestMapper;
import com.crawler.challenge.models.Crawl;
import com.crawler.challenge.services.CrawlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class CrawlHandler {

    public static Logger logger = LoggerFactory.getLogger(CrawlHandler.class);
    private final CrawlService crawlService;

    public CrawlHandler() {
        this.crawlService = CrawlService.getInstance();
    }

    public CreateCrawlResponse create(Request req, Response res) {
        logger.info("Creating and stating new crawl");
        var crawlReqObj = CreateCrawlRequestMapper.toDto(req);
        return crawlService.createCrawl(crawlReqObj);
    }

    public Crawl getById(Request req, Response res) {
        var crawlId = req.params("id");
        logger.info("Getting crawl with id: {}", crawlId);
        return crawlService.getCrawlById(crawlId);
    }
}
