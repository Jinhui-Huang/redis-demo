package com.myhd.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhd.pojo.Item;
import com.myhd.pojo.ItemStock;
import com.myhd.service.impl.ItemService;
import com.myhd.service.impl.ItemStockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * Description: RedisHandler
 * <br></br>
 * className: RedisHandler
 * <br></br>
 * packageName: com.myhd.config
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/11/12 20:53
 */
@Component
public class RedisHandler implements InitializingBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ItemService itemService;
    @Resource
    private ItemStockService itemStockService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception {
        /*初始化缓存*/
        /*1. 查询商品信息*/
        List<Item> items = itemService.list();
        for (Item item : items) {
            /*2.1. item序列化为jsons*/
            String itemJson = MAPPER.writeValueAsString(item);
            /*2. 放入Redis缓存*/
            stringRedisTemplate.opsForValue().set("item:id:" + item.getId(), itemJson, Duration.ofMinutes(5));
        }
        /*3. 查询库存信息*/
        List<ItemStock> itemStocks = itemStockService.list();
        for (ItemStock itemStock : itemStocks) {
            /*3.1. item序列化为jsons*/
            String itemStockJson = MAPPER.writeValueAsString(itemStock);
            /*4. 放入Redis缓存*/
            stringRedisTemplate.opsForValue().set("item:stock:id:" + itemStock.getId(), itemStockJson, Duration.ofMinutes(1));
        }
    }

    public void saveItem(Item item) {
        try {
            String json = MAPPER.writeValueAsString(item);
            stringRedisTemplate.opsForValue().set("item:id:" + item.getId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteItem(Item item) {
        stringRedisTemplate.delete("item:id:" + item.getId());
    }

    public void saveItemStock(ItemStock itemStock) {
        try {
            String json = MAPPER.writeValueAsString(itemStock);
            stringRedisTemplate.opsForValue().set("item:stock:id:" + itemStock.getId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteItemStock(ItemStock itemStock) {
        stringRedisTemplate.delete("item:stock:id:" + itemStock.getId());
    }
}
