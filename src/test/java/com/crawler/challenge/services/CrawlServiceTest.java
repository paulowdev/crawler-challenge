package com.crawler.challenge.services;

import com.crawler.challenge.dtos.CreateCrawlRequest;
import com.crawler.challenge.dtos.CreateCrawlResponse;
import com.crawler.challenge.models.Crawl;
import com.crawler.challenge.models.CrawlStatus;
import com.crawler.challenge.repositories.CrawlRepositoryImpl;
import com.crawler.challenge.repositories.ICrawlRepository;
import com.crawler.challenge.routes.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CrawlServiceTest {

    @Mock
    private ICrawlRepository crawlRepository;

    @InjectMocks
    private CrawlService crawlService;

    @Before
    public void setUp() {
        crawlService = CrawlService.getInstance();
        crawlRepository = new CrawlRepositoryImpl();
        MockitoAnnotations.openMocks(this);
        setMockRepository(crawlService, crawlRepository);
    }

    private void setMockRepository(CrawlService crawlService, ICrawlRepository crawlRepository) {
        try {
            var field = CrawlService.class.getDeclaredField("crawlRepository");
            field.setAccessible(true);
            field.set(crawlService, crawlRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateCrawl() {
        CreateCrawlRequest request = new CreateCrawlRequest("test");

        Crawl crawl = new Crawl("12345678", CrawlStatus.ACTIVE);
        when(crawlRepository.saveOrUpdate(any(Crawl.class))).thenReturn(crawl);

        CreateCrawlResponse response = crawlService.createCrawl(request);

        assertNotNull(response);
        assertEquals("12345678", response.getId());
        verify(crawlRepository, times(1)).saveOrUpdate(any(Crawl.class));
    }

    @Test
    public void testGetCrawlById() {
        Crawl crawl = new Crawl("12345678", CrawlStatus.ACTIVE);
        when(crawlRepository.get("12345678")).thenReturn(crawl);

        Crawl result = crawlService.getCrawlById("12345678");

        assertNotNull(result);
        assertEquals("12345678", result.getId());
        verify(crawlRepository, times(1)).get("12345678");
    }

    @Test
    public void testGetCrawlById_NotFound() {
        when(crawlRepository.get("12345678")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            crawlService.getCrawlById("12345678");
        });

        verify(crawlRepository, times(1)).get("12345678");
    }
}

