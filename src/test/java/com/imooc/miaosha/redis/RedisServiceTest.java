package com.imooc.miaosha.redis;

import com.imooc.miaosha.domain.MiaoshaUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {

        //redisService.set("111", "redis_111");

        redisService.set("test3", "test333333");

        redisTemplate.keys("*").stream().forEach(System.out::print);

    }


    @Test
    public void get() {
        //String data = redisService.get("test3", String.class);
        //log.info("redis_test:" + data);

        redisService.set(MiaoShaUserKey.token, "token", "token_test");

        log.info("miaosha-" + redisService.get(MiaoShaUserKey.token, "token", String.class));

    }

}