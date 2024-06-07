package com.crawler.challenge.core.databases;

import com.crawler.challenge.models.Crawl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrawlMemoryDB {
    private static volatile CrawlMemoryDB instance;
    public final Map<String, Crawl> dataMap;

    public CrawlMemoryDB() {
        this.dataMap = new ConcurrentHashMap<>();
    }

    public static CrawlMemoryDB getInstance() {
        var result = instance;
        if (result != null) {
            return result;
        }
        synchronized (CrawlMemoryDB.class) {
            if (instance == null) {
                instance = new CrawlMemoryDB();
            }
            return instance;
        }
    }
}
