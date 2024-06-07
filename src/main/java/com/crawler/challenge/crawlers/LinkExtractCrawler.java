package com.crawler.challenge.crawlers;

import com.crawler.challenge.core.configs.AppConfig;
import com.crawler.challenge.core.helpers.HtmlHelper;
import com.crawler.challenge.models.Crawl;
import com.crawler.challenge.models.CrawlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static com.crawler.challenge.core.clients.CustomHttpClient.getAsync;
import static com.crawler.challenge.core.helpers.HtmlHelper.urlMount;

public class LinkExtractCrawler {

    public static Logger logger = LoggerFactory.getLogger(LinkExtractCrawler.class);

    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final Set<String> extractedLinks = ConcurrentHashMap.newKeySet();
    private final ExecutorService executorService = Executors.newFixedThreadPool(AppConfig.DEFAULT_THREAD_POOL);
    private final String baseUrl;
    private final String keyword;
    private final Crawl crawl;

    public LinkExtractCrawler(String keyword, Crawl crawl) {
        this.baseUrl = AppConfig.BASE_URL;
        this.keyword = keyword;
        this.crawl = crawl;
    }

    public void run() {

        logger.info("Starting link extract crawler...");

        this.crawl(this.baseUrl).thenAccept((v) -> {
            if (this.crawl.getStatus() == CrawlStatus.ACTIVE) {
                this.crawl.setStatus(CrawlStatus.DONE);
            }
        }).join();
        executorService.shutdown();

    }

    CompletableFuture<Void> crawl(String url) {

        if (this.visitedUrls.contains(url) || this.extractedLinks.size() >= AppConfig.EXTRACT_LIMITER_VALUE) {
            return CompletableFuture.completedFuture(null);
        }

        logger.info("Visiting: {}", url);
        this.visitedUrls.add(url);

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return getAsync(url);
                    } catch (ExecutionException | InterruptedException | URISyntaxException e) {
                        logger.error("Error fetching the URL: " + url, e);
                        this.crawl.setStatus(CrawlStatus.FAILED);
                        e.printStackTrace();
                        return null;
                    }
                }, this.executorService)
                .thenCompose(htmlContent -> {

                    if (htmlContent.isEmpty()) {
                        return CompletableFuture.completedFuture(null);
                    }

                    try {
                        var htmlDoc = HtmlHelper.parseHtml(htmlContent);

                        if (!this.containsKeyword(htmlDoc, this.keyword)) {
                            return CompletableFuture.completedFuture(null);
                        }

                        var links = this.extractLinks(htmlDoc, url);
                        extractedLinks.addAll(links);
                        return CompletableFuture.allOf(links.stream()
                                .map(this::crawl).toArray(CompletableFuture[]::new));

                    } catch (Exception e) {
                        this.crawl.setStatus(CrawlStatus.FAILED);
                        e.printStackTrace();
                        return CompletableFuture.completedFuture(null);
                    }
                }).thenRun(() -> this.updateCrawlUrls(extractedLinks));
    }


    private boolean containsKeyword(Document htmlDoc, String content) {
        return HtmlHelper.hasContent(htmlDoc, content);
    }

    void updateCrawlUrls(Set<String> extractedLinks) {
        this.crawl.getUrls().addAll(extractedLinks);
    }

    Set<String> extractLinks(Document htmlDoc, String url) {
        Set<String> links = new HashSet<>();
        var nodeList = htmlDoc.getElementsByTagName("a");
        for (int i = 0; i < nodeList.getLength(); i++) {
            var node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                var element = (Element) node;
                String href = element.getAttribute("href");
                if (!href.isEmpty()) {
                    var absoluteUrl = urlMount(url, href);
                    if (absoluteUrl != null && absoluteUrl.startsWith(baseUrl)) {
                        links.add(absoluteUrl);
                    }
                }
            }
        }
        return links;
    }
}