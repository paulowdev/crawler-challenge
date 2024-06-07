package com.crawler.challenge.routes.filters;

import com.crawler.challenge.dtos.DefaultResponse;
import com.google.gson.Gson;
import spark.route.HttpMethod;

import static spark.Spark.*;

public class HttpFilter {

    public static void apply() {
        before("*", (req, res) -> {
            var httpMethod = req.requestMethod().toLowerCase();
            if (httpMethod.equals(HttpMethod.post.name()) && req.body().isEmpty()) {
                halt(400, new Gson().toJson(new DefaultResponse("Invalid payload")));
            }
        });
        afterAfter((req, res) -> {
            res.type("application/json");
            res.header("Content-Encoding", "gzip");
        });
    }
}
