package com.myhd.canal;

import com.github.benmanes.caffeine.cache.Cache;
import com.myhd.config.RedisHandler;
import com.myhd.pojo.ItemStock;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;

/**
 * Description: ItemSrockHandler
 * <br></br>
 * className: ItemSrockHandler
 * <br></br>
 * packageName: com.myhd.canal
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/11/13 19:24
 */
@CanalTable("tb_item_stock")
@Component
public class ItemStockHandler implements EntryHandler<ItemStock> {

    @Resource
    private RedisHandler redisHandler;

    @Resource
    private Cache<Long, ItemStock> itemStockCache;

    /**
     * Description: insert 发生数据插入
     * @return void
     * @author jinhui-huang
     * @Date 2023/11/13
     * */
    @Override
    public void insert(ItemStock itemStock) {
        /*写数据到JVM进程缓存*/
        itemStockCache.put(itemStock.getId(), itemStock);
        /*写数据到redis*/
        redisHandler.saveItemStock(itemStock);
    }

    /**
     * Description: update 发生数据更新
     * @return void
     * @author jinhui-huang
     * @Date 2023/11/13
     * */
    @Override
    public void update(ItemStock before, ItemStock after) {
        /*写数据到JVM进程缓存*/
        itemStockCache.put(after.getId(), after);
        /*写数据到redis*/
        redisHandler.saveItemStock(after);

    }

    /**
     * Description: delete 发生数据删除
     * @return void
     * @author jinhui-huang
     * @Date 2023/11/13
     * */
    @Override
    public void delete(ItemStock itemStock) {
        /*删除JVM进程数据*/
        itemStockCache.invalidate(itemStock.getId());
        /*删除redis数据*/
        redisHandler.deleteItemStock(itemStock);
    }
}