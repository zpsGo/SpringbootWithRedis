package com.zps.bootwithredis.contorller;

import com.zps.bootwithredis.service.RedisImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: bootwithredis
 * @description: 下单控制处理
 * @author: Mr.Wang
 * @create: 2020-04-13 15:19
 **/
@RestController
public class OrderController {

    @Autowired
    RedisImp redisImp;

    @PostMapping("order")
    public String order(@RequestParam("count") int count){
        String rs = redisImp.updateStore(count);
        System.out.println(rs);
        return rs;
    }
}
