package com.nasir.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class RateLimiter {
    //autowiring dependencies

    private final ProxyManager<String> buckets;
    private final UserService userService;

    public RateLimiter(ProxyManager<String> buckets,
                       UserService userService) {
        this.buckets = buckets;
        this.userService = userService;
    }

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);
        return buckets.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {
        UserLimit userLimit = userService.getUser(key);
        Refill refill = Refill.intervally(userLimit.getRateLimit(), Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(userLimit.getRateLimit(), refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}