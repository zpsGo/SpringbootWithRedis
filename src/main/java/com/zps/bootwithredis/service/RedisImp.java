package com.zps.bootwithredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @program: bootwithredis
 * @description: 预减库存Imp类
 * @author: Mr.Wang
 * @create: 2020-04-13 14:57
 **/
@Service
public class RedisImp {

    @Autowired
    RedisService redisService;
    @Autowired
    StringRedisTemplate redisTemplate;

    //设置过期时间
    private static final int TIMEOUT = 5 * 1000;
    //设置库存量为20000，模仿数据库库存
    private static final int STORE = 20000;

    public String updateStore(int count){
        try {
            //设置过期时间
            long time = System.currentTimeMillis() + TIMEOUT;
            //获取锁失败
            if (!redisService.luck("closeKey", String.valueOf(time))) {
                return "前方拥挤，请稍后再试！";
            }
            //获取锁成功，先判断库存是否被加载到了redis
            String store = redisTemplate.opsForValue().get("close");
            if (StringUtils.isEmpty(store)) {
                redisTemplate.opsForValue().set("close", "20000");
            }
            if (Integer.parseInt(store) <= 0)
                return "库存不足！";
            if (Integer.parseInt(store) <= count)
                return "库存不足以您的购买够买量" + "当前剩下：" + Integer.parseInt(store) + "您的购买量为" + count;
            //预减redis库存
            redisTemplate.opsForValue().set("close", String.valueOf(Integer.parseInt(store) - count));
            //此处可以通过消息中间件发送订单消息
            //rabbitmqService.sendMsg(XXX);

            //最后释放锁
            redisService.unlock("closeKey", String.valueOf(time));
        }catch (Exception e){
            throw new RuntimeException("下单异常！");
        }
        return "下单成功！";
    }
}
