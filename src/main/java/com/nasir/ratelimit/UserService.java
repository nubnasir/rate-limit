package com.nasir.ratelimit;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {

    HashMap<String, UserLimit> userLimitHashMap = new HashMap<>();

    public UserService() {
        userLimitHashMap.put("nasir", new UserLimit("nasir", 5L));
        userLimitHashMap.put("saddam", new UserLimit("saddam", 5L));
    }

    public UserLimit getUser(String key) {
        if(userLimitHashMap.containsKey(key)){
            return userLimitHashMap.get(key);
        }
        UserLimit userLimit = new UserLimit(key, 5L);
        userLimitHashMap.put(key, userLimit);
        return userLimit;
    }
}
