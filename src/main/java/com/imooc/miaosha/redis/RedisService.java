package com.imooc.miaosha.redis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public <T> T get(String key, Class<T> clazz) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String obj = valueOperations.get(key);
        T t = stringToBean(obj, clazz);
        return t;
    }

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        //生成真正的key
        String realKey = prefix.getPrefix() + key;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String obj = valueOperations.get(realKey);
        T t = stringToBean(obj, clazz);
        return t;
    }


    public <T> boolean set(final KeyPrefix prefix, String key, T value) {

        String str = beanToString(value);

        if (str == null || str.length() <= 0) return false;

        //生成真正的key
        String realKey = prefix.getPrefix() + key;
        int seconds = prefix.expireSeconds();

        if (seconds <= 0) {
            redisTemplate.opsForValue().set(realKey, str);
        } else {
            redisTemplate.opsForValue().set(realKey, str, seconds);
        }

        return true;

    }

    public <T> boolean set(final String key, T value) {

        boolean result = false;

        try {

            String str = beanToString(value);
            redisTemplate.opsForValue().set(key, str);
            result = true;
        } catch (Exception e) {
            log.error("set error: key {}, value {}", key, value, e);
        }

        return result;
    }


    public static <T> String beanToString(T value) {
        if (value == null) return null;

        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {

            return String.valueOf(value);
        } else if (clazz == String.class) {

            return String.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {

            return String.valueOf(value);

        } else {
            return JSON.toJSONString(value);
        }

    }


    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


}
