package com.crawler.challenge.repositories;

import com.crawler.challenge.models.Crawl;

public interface ICrawlRepository {
    Crawl get(String id);

    Crawl saveOrUpdate(Crawl data);
}
