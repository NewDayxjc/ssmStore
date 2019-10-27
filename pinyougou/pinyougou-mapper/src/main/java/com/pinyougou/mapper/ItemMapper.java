package com.pinyougou.mapper;

import com.pinyougou.model.Item;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ItemMapper extends Mapper<Item> {
    //批量增加items
    void batchInsert(List<Item> items);
}