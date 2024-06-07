package com.crawler.challenge.routes;

import com.crawler.challenge.routes.exceptions.ValidationServerException;
import com.crawler.challenge.routes.filters.HttpFilter;
import com.crawler.challenge.core.transformers.JsonTransformer;
import com.crawler.challenge.dtos.DefaultResponse;
import com.crawler.challenge.routes.exceptions.NotFoundException;
import com.google.gson.Gson;
import spark.ResponseTransformer;

import static spark.Spark.exception;

public abstract class AbstractRoute {

    abstract void filters();

    abstract void routes();

    protected static ResponseTransformer getTransformer() {
        return new JsonTransformer();
    }

    private void defaultFilters() {
        HttpFilter.apply();
    }

    private void defaultExceptionMappings() {

        exception(NotFoundException.class, (e, req, res) -> {
            res.status(404);
            var errMsg = e.getMessage().isEmpty() ? "Resource not found" : e.getMessage();
            res.body(new Gson().toJson(new DefaultResponse(errMsg)));
        });
        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            var errMsg = e.getMessage().isEmpty() ? "Internal Server Error" : e.getMessage();
            res.body(new Gson().toJson(new DefaultResponse(errMsg)));
        });
        exception(ValidationServerException.class, (e, req, res) -> {
            res.status(512);
            var errMsg = e.getMessage().isEmpty() ? "Internal Server Error" : e.getMessage();
            res.body(new Gson().toJson(new DefaultResponse(errMsg)));
        });
    }

    public void use() {
        this.defaultFilters();
        this.defaultExceptionMappings();
        this.filters();
        this.routes();
    }
}
