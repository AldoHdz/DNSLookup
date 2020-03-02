package com.evocalize.dnslookup.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {
    public CacheManager cacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("results");
        caffeineCacheManager.setCaffeine(caffineCacheBuilder());
        return caffeineCacheManager;
    }

    Caffeine<Object, Object> caffineCacheBuilder(){
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .weakKeys()
                .recordStats();
    }
}
