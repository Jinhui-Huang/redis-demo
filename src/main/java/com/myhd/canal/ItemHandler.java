package com.myhd.canal;

import com.github.benmanes.caffeine.cache.Cache;
import com.myhd.config.RedisHandler;
import com.myhd.pojo.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;

/**
 * Description: ItemHandler
 * <br></br>
 * className: ItemHandler
 * <br></br>
 * packageName: com.myhd.canal
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/11/13 19:14
 */
@CanalTable("tb_item")
@Component
@Slf4j
public class ItemHandler implements EntryHandler<Item> {

    @Resource
    private RedisHandler redisHandler;

    @Resource
    private Cache<Long, Item> itemCache;

    /**
     * Description: insert 发生数据插入
     * @return void
     * @author jinhui-huang
     * @Date 2023/11/13
     * */
    @Override
    public void insert(Item item) {
        log.info("发生数据插入");
        /*写数据到JVM进程缓存*/
        itemCache.put(item.getId(), item);
        /*写数据到redis*/
        redisHandler.saveItem(item);
    }

    /**
     * Description: update 发生数据更新
     * @return void
     * @author jinhui-huang
     * @Date 2023/11/13
     * */
    @Override
    public void update(Item before, Item after) {
        log.info("发生数据更新");
        /*写数据到JVM进程缓存*/
        itemCache.put(after.getId(), after);
        /*写数据到redis*/
        redisHandler.saveItem(after);
    }

    /**
     * Description: delete 发生数据删除
     * @return void
     * @author jinhui-huang
     * @Date 2023/11/13
     * */
    @Override
    public void delete(Item item) {
        log.info("发生数据删除");
        /*删除JVM进程数据*/
        itemCache.invalidate(item.getId());
        /*删除redis数据*/
        redisHandler.deleteItem(item);
    }
}
