package com.crawler.challenge.routes;

import com.crawler.challenge.JavaSparkRunnerExtension;
import com.crawler.challenge.clients.HttpClientIntegration;
import com.crawler.challenge.dtos.CreateCrawlRequest;
import com.crawler.challenge.dtos.CreateCrawlResponse;
import com.crawler.challenge.handlers.CrawlHandler;
import com.crawler.challenge.core.helpers.StringHelper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static spark.Spark.awaitInitialization;

@ExtendWith(JavaSparkRunnerExtension.class)
public class CrawlRouteTest {

    private static final String SERVER_URL = "http://localhost:4567";

    private ExecutorService executorService;

    private HttpClient client;

    @BeforeAll
    static void beforeAll(JavaSparkRunnerExtension.SparkStarter s) {
        s.runSpark(http -> {
        });
    }

    @AfterEach
    void tearDown() {
        executorService.shutdown();
    }

    @Before
    public void setUp() {
        client = HttpClientIntegration.getHttpClient();
        new CrawlRoute(new CrawlHandler()).use();
        awaitInitialization();
    }

    @Test
    public void givenValidPayload_shouldCreateCrawl_Successfully() throws URISyntaxException, IOException, InterruptedException {

        var payload = new CreateCrawlRequest("test");
        var httpPostReq = HttpRequest.newBuilder()
                .uri(new URI(SERVER_URL.concat("/crawl")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(payload)))
                .build();

        var response = client.send(httpPostReq, HttpResponse.BodyHandlers.ofString());

        var resObj = new Gson().fromJson(response.body(), CreateCrawlResponse.class);
        assertEquals(200, response.statusCode());
        Assertions.assertTrue(StringHelper.isAlphaNumeric(resObj.getId()));
    }

    @Test
    public void givenInvalidPayload_shouldThrow_BadRequest() throws URISyntaxException, IOException, InterruptedException {

        var payload = new CreateCrawlRequest("tes");
        var httpPostReq = HttpRequest.newBuilder()
                .uri(new URI(SERVER_URL.concat("/crawl")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(payload)))
                .build();

        var response = client.send(httpPostReq, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    public void givenInvalidCrawlId_shouldThrow_BadRequest() throws URISyntaxException, IOException, InterruptedException {

        var invalidId = "@!6689";
        var httpGetReq = HttpRequest.newBuilder()
                .uri(new URI(SERVER_URL.concat("/crawl/".concat(invalidId)))).GET().build();

        var response = client.send(httpGetReq, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    public void givenValidCrawlId_shouldThrow_NotFound() throws URISyntaxException, IOException, InterruptedException {

        var validId = "uBR991iN";
        var httpGetReq = HttpRequest.newBuilder()
                .uri(new URI(SERVER_URL.concat("/crawl/".concat(validId)))).GET().build();

        var response = client.send(httpGetReq, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
