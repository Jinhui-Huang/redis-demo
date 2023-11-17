package com.myhd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.myhd.mapper.ItemMapper;
import com.myhd.pojo.Item;
import com.myhd.service.IItemService;
import com.myhd.service.IItemStockService;
import com.myhd.pojo.ItemStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.function.Function;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {
    @Resource
    private IItemStockService stockService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static boolean FLAG = false;

    @Resource
    private Cache<Long, Item> itemCache;

    @Override
    @Transactional
    public void saveItem(Item item) {
        // 新增商品
        save(item);
        // 新增库存
        ItemStock stock = new ItemStock();
        stock.setId(item.getId());
        stock.setStock(item.getStock());
        stockService.save(stock);
    }

    @Override
    public Item queryItem(Long id) {
        String itemJson;// stringRedisTemplate.opsForValue().get("item:id:" + id);
        try {
            /*if (itemJson != null) {
                return MAPPER.readValue(itemJson, Item.class);
            }*/

            Item item = itemCache.get(id, key -> {
                FLAG = true;
                return query().ne("status", 3).eq("id", id).one();
            });
            if (item == null) return null;
            if (FLAG) itemCache.put(item.getId(), item);
            itemJson = MAPPER.writeValueAsString(item);
            stringRedisTemplate.opsForValue().set("item:id:" + id, itemJson, Duration.ofMinutes(5));
            return item;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
