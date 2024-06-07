package com.crawler.challenge.core.configs;

import java.util.Optional;

public class AppConfig {

    public static final int DEFAULT_THREAD_POOL = Thread.activeCount();
    public static final String BASE_URL = Optional.ofNullable(System.getenv("BASE_URL")).orElse("");
    public static final int EXTRACT_LIMITER_VALUE = Integer.parseInt(Optional.ofNullable(System.getenv("EXTRACT_LIMITER_VALUE")).orElse("150"));

}
