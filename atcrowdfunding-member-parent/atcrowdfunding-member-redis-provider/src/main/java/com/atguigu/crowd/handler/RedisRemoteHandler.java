package com.atguigu.crowd.handler;

import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class RedisRemoteHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ValueOperations<String,String> valueOperations;

    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key, @RequestParam("value") String value){
        try{
            valueOperations.set(key,value);
            return ResultEntity.successWithOutData();
        }catch (Exception e){
            log.info("----------->redis 插入失败" + " key = " + key + "; value = " + value + ";");
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(@RequestParam("key") String key,
                                                           @RequestParam("value") String value,
                                                           @RequestParam("time") long time,
                                                           @RequestParam("timeUnit") TimeUnit timeUnit){
        try{
            valueOperations.set(key,value,time,timeUnit);
            return ResultEntity.successWithOutData();
        }catch (Exception e){
            log.info("----------->redis 过期插入失败" + " key = " + key + "; value = " + value + ";");
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key){
        try{
            String value = valueOperations.get(key);
            return ResultEntity.successWithData(value);
        }catch (Exception e){
            log.info("----------->redis 该key不存在，查询失败" + " key = " + key);
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key){
        try{
            redisTemplate.delete(key);
            return ResultEntity.successWithOutData();
        }catch (Exception e){
            log.info("----------->redis 该key不存在，删除失败" + " key = " + key);
            return ResultEntity.failed(e.getMessage());
        }
    }
}
