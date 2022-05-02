package com.nasir.ratelimit.aop;

import com.nasir.ratelimit.RateLimiter;
import com.nasir.ratelimit.exception.LimitExceededException;
import com.nasir.ratelimit.exception.UnauthorizedException;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {
    private final RateLimiter rateLimiter;

    public RateLimitAspect(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Pointcut("@annotation(com.nasir.ratelimit.RateLimit)")
    public void limitRate() {}

    @Before("limitRate()")
    public void logMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        log.info("### methodName: " + methodName);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String header = request.getHeader("Authorization");
        log.info("### header: " + header);
        if(header ==null || header.length() == 0){
            throw new UnauthorizedException("Unauthorized request");
        }

        Bucket bucket = rateLimiter.resolveBucket(header.substring(7));

        if(!bucket.tryConsume(1)){
            throw new LimitExceededException("You reached your limit");
        }
    }

}
