package com.csu.mall.util;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //超过最大空间会用LRU算法
    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().
            initialCapacity(1000).maximumSize(5000).expireAfterAccess(1, TimeUnit.HOURS).
            build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });

    public static void setToken(String key, String value) {
        loadingCache.put(key, value);
    }

    public static String getToken(String key){
        String token = null;
        try{
            token = loadingCache.get(key);
        }catch (Exception e){
           logger.error("Guava Cache Error", e);
        }
        return token;
    }



}
