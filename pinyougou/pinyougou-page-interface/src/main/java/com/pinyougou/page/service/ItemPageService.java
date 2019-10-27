package com.pinyougou.page.service;

import java.io.IOException;

/**
 * @Author xiongjinchen
 * @Date 2019/9/29 11:49
 * @Version 1.0
 *
 * @param  ;Good,GoodsDesc List<Item> 把goodsId作为静态页名字
 */

public interface ItemPageService {
    /**
     * 根据GoodsId生成静态页
     * @param goodsId
     * @return
     * @throws Exception
     */
    public Boolean buildHtml(Long goodsId) throws  Exception;

    /**
     * 根据goodsId删除已生成静态页
     * @param id
     */
    void deleteHtml(Long id);
}
