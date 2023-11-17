package com.myhd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myhd.pojo.ItemStock;

public interface IItemStockService extends IService<ItemStock> {
    ItemStock getItemStockById(Long id);
}
