package com.crawler.challenge.core.transformers;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private final Gson gson = new Gson();

    @Override
    public String render(Object obj) {
        return this.gson.toJson(obj);
    }
}
