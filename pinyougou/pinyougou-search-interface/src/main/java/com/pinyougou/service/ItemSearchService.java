package com.pinyougou.service;

import com.pinyougou.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author xiongjinchen
 * @Date 2019/9/25 0:34
 * @Version 1.0
 */

public interface ItemSearchService {

    /**
     *搜索方法
     * 传入的参数：Map
     * 传入的参数
     * 关键词
     *      品牌     规格
     *      价格   分类   排序
     * 响应数据：map
     *      商品列表
     *      分页数据
     *
     *
     *
     */

    Map<String,Object> search(Map search);

    /**
     * 将根据goodsId查询出来的item批量导入索引库
     * @param itemList
     */
    void importItemist(List<Item> itemList);

    void DeleteItemByIds(List<Long> ids);
}
