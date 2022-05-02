package com.nasir.ratelimit.controller;

import com.nasir.ratelimit.RateLimit;
import com.nasir.ratelimit.RateLimiter;
import com.nasir.ratelimit.model.MessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RateLimit
    @GetMapping("/api/v1/access/limited-data")
    public MessageDto getData() {
        return new MessageDto("Limited access configured resource");
    }
}
