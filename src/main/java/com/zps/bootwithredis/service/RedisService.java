package com.zps.bootwithredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RedisService {

    //该类继承了RedisTemplate,默认实现了字符串序列化，
    // 如果需要实现对象序列化，可以重新注入一个RedisTemplate
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public boolean luck(String lockKey , String timeStamp){

        //尝试获取锁，如果该key锁已经存在，返回false;若没有，设置key,value，并返回true;
        // 对应setnx命令，可以成功设置,也就是key不存在，获得锁成功
        if(stringRedisTemplate.opsForValue().setIfAbsent(lockKey , timeStamp)){
            return true;
        }

        //获取锁判断该锁是否而应某种原因没有释放锁而导致已经过期
        //获取key的过期时间
        String currentLock = stringRedisTemplate.opsForValue().get(lockKey);

        //若过期，重新设置锁
        if(!StringUtils.isEmpty(currentLock) && Long.parseLong(currentLock) < System.currentTimeMillis()){
            //设值
            String secondtiem = stringRedisTemplate.opsForValue().getAndSet(lockKey , timeStamp);
            //判断是否出现并发的情况，若没有就返回true
            if(!StringUtils.isEmpty(secondtiem) && timeStamp.equals(secondtiem)){
                return true;
            }
        }
        return false;
    }

    //释放锁
    public void unlock(String lockKye , String timeStamp){
        try {
            //获取锁的时间戳
            String pretimeStamp = stringRedisTemplate.opsForValue().get(lockKye);
            //判断是否过期
            if (!StringUtils.isEmpty(pretimeStamp) && timeStamp.equals(pretimeStamp)) {
                stringRedisTemplate.opsForValue().getOperations().delete(lockKye);
            }
        }catch (Exception e){
            throw new RuntimeException("解除异常！");
        }
    }
}
