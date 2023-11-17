package com.myhd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myhd.pojo.Item;

public interface IItemService extends IService<Item> {
    void saveItem(Item item);

    Item queryItem(Long id);
}
