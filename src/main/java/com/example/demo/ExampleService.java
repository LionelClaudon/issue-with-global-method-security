package com.example.demo;

import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    @Cacheable(value = "example")
    public UUID getCacheableString(UUID key) {
        return UUID.randomUUID();
    }
}
