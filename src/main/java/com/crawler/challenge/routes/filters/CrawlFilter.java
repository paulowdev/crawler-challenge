package com.crawler.challenge.routes.filters;

import com.crawler.challenge.dtos.DefaultResponse;
import com.crawler.challenge.dtos.mappers.CreateCrawlRequestMapper;
import com.crawler.challenge.core.helpers.StringHelper;
import com.google.gson.Gson;

import java.util.Optional;

import static spark.Spark.before;
import static spark.Spark.halt;

public class CrawlFilter {

    public static void apply() {

        before("/crawl", (req, res) -> {

            var crawlReqObj = CreateCrawlRequestMapper.toDto(req);

            var strKeyword = Optional.ofNullable(crawlReqObj.getKeyword()).orElse("");
            var strKeywordLength = strKeyword.length();

            if (strKeyword.isEmpty()) {
                halt(400, new Gson().toJson(new DefaultResponse(" 'keyword' attribute is invalid")));
            }
            if ((strKeywordLength < 4) || (strKeywordLength > 32)) {
                halt(400, new Gson().toJson(new DefaultResponse(" 'keyword' attribute must have a minimum of 4 and a maximum of 32 characters")));
            }

        });

        before("/crawl/:id", (req, res) -> {
            var idParam = req.params("id");
            if (!StringHelper.isAlphaNumeric(idParam) || (idParam.length() < 8)) {
                halt(400, new Gson().toJson(new DefaultResponse("Invalid ID")));
            }
        });

    }
}
