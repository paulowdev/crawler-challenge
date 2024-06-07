package com.crawler.challenge.repositories;

import com.crawler.challenge.core.databases.CrawlMemoryDB;
import com.crawler.challenge.models.Crawl;

public class CrawlRepositoryImpl implements ICrawlRepository {

    private final CrawlMemoryDB crawlMemoryDB;

    public CrawlRepositoryImpl() {
        this.crawlMemoryDB = CrawlMemoryDB.getInstance();
    }

    @Override
    public Crawl get(String id) {
        return this.crawlMemoryDB.dataMap.get(id);
    }

    @Override
    public Crawl saveOrUpdate(Crawl data) {
        this.crawlMemoryDB.dataMap.put(data.getId(), data);
        return this.get(data.getId());
    }
}
