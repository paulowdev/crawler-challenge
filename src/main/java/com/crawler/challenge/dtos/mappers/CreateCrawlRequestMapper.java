package com.crawler.challenge.dtos.mappers;

import com.crawler.challenge.dtos.CreateCrawlRequest;
import com.google.gson.Gson;
import spark.Request;

public class CreateCrawlRequestMapper {

    public static CreateCrawlRequest toDto(Request req){
        return new Gson().fromJson(req.body(), CreateCrawlRequest.class);
    }
}
