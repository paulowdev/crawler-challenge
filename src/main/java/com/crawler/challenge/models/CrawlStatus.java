package com.crawler.challenge.models;

import com.google.gson.annotations.SerializedName;

public enum CrawlStatus {
    @SerializedName("active")
    ACTIVE("active"),
    @SerializedName("done")
    DONE("done"),
    @SerializedName("failed")
    FAILED("failed");

    private final String status;

    CrawlStatus(final String s) {
        status = s;
    }

    public String toString() {
        return status;
    }
}
