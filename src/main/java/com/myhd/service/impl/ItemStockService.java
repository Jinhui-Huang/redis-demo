package com.myhd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.myhd.service.IItemStockService;
import com.myhd.mapper.ItemStockMapper;
import com.myhd.pojo.ItemStock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

@Service
public class ItemStockService extends ServiceImpl<ItemStockMapper, ItemStock> implements IItemStockService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Resource
    private Cache<Long, ItemStock> stockCache;

    private static boolean FLAG = false;


    @Override
    public ItemStock getItemStockById(Long id) {
        String itemStockJson; // stringRedisTemplate.opsForValue().get("item:stock:id:" + id);
        try {
            /*if (itemStockJson != null) {
                return MAPPER.readValue(itemStockJson, ItemStock.class);
            }*/
            ItemStock stock = stockCache.get(id, key -> {
                FLAG = true;
                return getById(id);
            });
            if (stock == null) return null;
            if (FLAG) stockCache.put(stock.getId(), stock);
            itemStockJson = MAPPER.writeValueAsString(stock);
            stringRedisTemplate.opsForValue().set("item:stock:id:" + id, itemStockJson, Duration.ofMinutes(1));
            return stock;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
