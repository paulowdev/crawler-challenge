package com.crawler.challenge.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TestHelper {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static String getHtmlResource(String resourceName) throws IOException {
        Path resourceDirectory = Path.of(Paths.get("src", "test", "resources").toUri());
        String fullAbsolutePath = resourceDirectory.toFile().getAbsolutePath() + "/" + resourceName;
        File file = new File(fullAbsolutePath);
        URL url = new URL(file.toURI().toString());
        try (InputStream inputStream = url.openStream()) {
            return convertInputStreamToString(inputStream);
        }
    }

    public static void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }

    private static String convertInputStreamToString(InputStream is) throws IOException {

        var result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }
}
