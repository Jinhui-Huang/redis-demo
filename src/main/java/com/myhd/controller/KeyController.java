package com.myhd.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.myhd.util.Result;

import javax.annotation.Resource;

/**
 * Description: KeyController
 * <br></br>
 * className: controller
 * <br></br>
 * packageName: com
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/11/5 18:05
 */
@RestController
@RequestMapping("/key")
public class KeyController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/get/{key}")
    public Result getKey(@PathVariable String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return new Result(10001, value, "请求成功");
    }

    @GetMapping("/set/{key}/{value}")
    public Result getKeys(@PathVariable String key, @PathVariable String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return new Result(10001, null, "设置key成功");
    }
}
