package com.crawler.challenge.services;

import com.crawler.challenge.core.configs.AppConfig;
import com.crawler.challenge.core.helpers.StringHelper;
import com.crawler.challenge.crawlers.LinkExtractCrawler;
import com.crawler.challenge.dtos.CreateCrawlRequest;
import com.crawler.challenge.dtos.CreateCrawlResponse;
import com.crawler.challenge.models.Crawl;
import com.crawler.challenge.models.CrawlStatus;
import com.crawler.challenge.repositories.CrawlRepositoryImpl;
import com.crawler.challenge.repositories.ICrawlRepository;
import com.crawler.challenge.routes.exceptions.NotFoundException;
import com.crawler.challenge.routes.exceptions.ValidationServerException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class CrawlService {

    private static CrawlService instance;

    private final ICrawlRepository crawlRepository;

    private CrawlService() {
        this.crawlRepository = new CrawlRepositoryImpl();
    }

    public synchronized static CrawlService getInstance() {
        if (instance == null) {
            instance = new CrawlService();
        }
        return instance;
    }

    public CreateCrawlResponse createCrawl(final CreateCrawlRequest crawlReqObj) {

        if (!this.isValidBaseUrl()) {
            throw new ValidationServerException("Base url is empty or invalid.");
        }

        var keyword = crawlReqObj.getKeyword().toLowerCase();
        var crawlObj = crawlRepository.saveOrUpdate(new Crawl(StringHelper.genRandomAlphaNum(8), CrawlStatus.ACTIVE));
        var linkExtractCrawler = new LinkExtractCrawler(keyword, crawlObj);

        CompletableFuture.runAsync(linkExtractCrawler::run);

        return new CreateCrawlResponse(crawlObj.getId());
    }

    public Crawl getCrawlById(String id) {
        return Optional.ofNullable(crawlRepository.get(id))
                .orElseThrow(() -> new NotFoundException("Crawl not found."));
    }

    private boolean isValidBaseUrl() {
        try {
            new URL(AppConfig.BASE_URL).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
