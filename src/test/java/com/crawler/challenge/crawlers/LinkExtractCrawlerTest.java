package com.crawler.challenge.crawlers;

import com.crawler.challenge.mocks.NodeListMock;
import com.crawler.challenge.models.Crawl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LinkExtractCrawlerTest {

    @Mock
    private Crawl crawl;

    @Mock
    private Document htmlDoc;

    @InjectMocks
    private LinkExtractCrawler linkExtractCrawler;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testContainsKeyword() {
//        when(HtmlHelper.hasContent(htmlDoc, "keyword")).thenReturn(true);
//
//        boolean result = linkExtractCrawler.containsKeyword(htmlDoc, "keyword");
//
//        assertTrue(result);
//    }

    @Test
    public void testExtractLinks() {
        when(htmlDoc.getElementsByTagName("a")).thenReturn(new NodeListMock());

        Set<String> links = linkExtractCrawler.extractLinks(htmlDoc, "http://example.com");

        assertNotNull(links);
    }

    @Test
    public void testUpdateCrawlUrls() {
        Set<String> extractedLinks = Set.of("http://example.com/link1", "http://example.com/link2");

        linkExtractCrawler.updateCrawlUrls(extractedLinks);

        verify(crawl, times(1)).getUrls();
    }

//    @Test
//    public void testCrawl() throws Exception {
//        when(HtmlHelper.parseHtml(anyString())).thenReturn(htmlDoc);
//        when(HtmlHelper.hasContent(htmlDoc, "keyword")).thenReturn(true);
//        when(htmlDoc.getElementsByTagName("a")).thenReturn(new NodeListMock());
//
//        CompletableFuture<Void> future = linkExtractCrawler.crawl("http://example.com");
//        future.join();
//
//    }

////    @Test
//    public void testRun() {
//        doNothing().when(crawl).setStatus(any(CrawlStatus.class));
//
//        linkExtractCrawler.run();
//
//        verify(crawl, times(1)).setStatus(CrawlStatus.DONE);
//    }
}




