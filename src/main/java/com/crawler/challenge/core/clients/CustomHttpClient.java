package com.crawler.challenge.core.clients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CustomHttpClient {

    private static final int HTTP_DEFAULT_TIMEOUT = 60;
    private static final int HTTP_DEFAULT_THREAD_POOL = 4;
    private static final int HTTP_MAX_RETRY = 3;

    private static HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(HTTP_DEFAULT_TIMEOUT))
                .executor(Executors.newFixedThreadPool(HTTP_DEFAULT_THREAD_POOL))
                .build();
    }

    public static String getSync(final String url) throws IOException, InterruptedException, URISyntaxException {

        var httpReq = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        var httpRes = getHttpClient().send(httpReq, HttpResponse.BodyHandlers.ofString());

        return (httpRes.statusCode() == 200) ? httpRes.body() : "";
    }

    public static String getAsync(final String url) throws InterruptedException, URISyntaxException, ExecutionException {

        var httpReq = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
        var resHandler = HttpResponse.BodyHandlers.ofString();
        var httpRes = getHttpClient().sendAsync(httpReq, resHandler)
                .thenComposeAsync(r -> retry(getHttpClient(), httpReq, resHandler, 1, r)).join();
        return httpRes.body();
    }

    public static <T> CompletableFuture<HttpResponse<T>> retry(HttpClient client, HttpRequest request, HttpResponse.BodyHandler<T> handler,
                                                               int count, HttpResponse<T> resp) {
        if (resp.statusCode() == 200 || count >= HTTP_MAX_RETRY) {
            return CompletableFuture.completedFuture(resp);
        } else {
            return client.sendAsync(request, handler)
                    .thenComposeAsync(r -> retry(client, request, handler, count + 1, r));
        }
    }
}
