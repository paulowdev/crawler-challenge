package com.crawler.challenge.clients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;

public class HttpClientIntegration {

    public static HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .executor(Executors.newFixedThreadPool(4))
                .build();
    }

    public static HttpResponse<String> get(final String url) throws IOException, InterruptedException, URISyntaxException {

        var httpReq = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        return getHttpClient().send(httpReq, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> post(final String url, final String payload) throws IOException, InterruptedException, URISyntaxException {

        var httpReq = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return getHttpClient().send(httpReq, HttpResponse.BodyHandlers.ofString());
    }
}
