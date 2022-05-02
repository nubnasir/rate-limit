package com.nasir.ratelimit;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.config.Config;
import org.redisson.jcache.JCachingProvider;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;

@Configuration
public class Bucket4jRedisConfig {

    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Bean
    public Config config() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + REDIS_HOST + ":6379");
        return config;
    }

    @Bean("RedisCaching")
    public CachingProvider redisCachingProvider() {
        return new JCachingProvider();
    }

    @Bean
    ProxyManager<String> proxyManager(@Qualifier("RedisCaching") CachingProvider cachingProvider, Config config) {
        CacheManager manager = cachingProvider.getCacheManager();
        manager.createCache("accounting-api-rate-limit-cache", RedissonConfiguration.fromConfig(config));

        return new JCacheProxyManager(manager.getCache("accounting-api-rate-limit-cache"));
    }
}