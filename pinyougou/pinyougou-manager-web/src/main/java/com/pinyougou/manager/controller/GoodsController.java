package com.pinyougou.manager.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.http.Result;
import com.pinyougou.manager.service.MessageSender;
import com.pinyougou.model.Goods;
import com.pinyougou.model.Item;
import com.pinyougou.mq.MessageInfo;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;
    @Autowired
    private MessageSender messageSender;

    /***
     * 根据ID批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete")
    public Result delete(@RequestBody List<Long> ids){
        try {
            //根据ID删除数据
            int dcount = goodsService.deleteByIds(ids);

            if(dcount>0){
                //发送信息  调用ActiveMQ
                MessageInfo messageInfo=new MessageInfo(MessageInfo.METHOD_DELETE,ids);

                messageSender.sendObjectMessage(messageInfo);

                return new Result(true,"删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"删除失败");
    }

    /**
     *审核商品功能
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping(value = "/update/status",method = RequestMethod.POST)
    public Result updateStatus(@RequestBody List<Long> ids,@RequestParam(value = "status") String status){
        try {
            int amount = goodsService.updateStatus(ids, status);
            if (amount > 0) {
                //根据goodsId获取到sku信息并将其更新到索引库 必须是审核通过的
                if(status.equals("1")) {
                    //如果商品审核状态是1 即通过
                    //根据goodsId获取到sku信息(Item)
                     List<Item> itemList= goodsService.getItemByIdAndStatus(ids,status);
//
//                    for (Long goodsId : ids) {
//                        //审核完成，生成静态页
//                        itemPageService.buildHtml(goodsId);
//                    }

                    //将查询出来的item信息批量导入索引库
//                    itemSearchService.importItemist(itemList);
                    //发送
                   MessageInfo messageInfo=new MessageInfo(MessageInfo.METHOD_UPDATE,itemList);
                   messageSender.sendObjectMessage(messageInfo);
                }
                return new Result(true, "审核成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
                return new Result(false, "审核失败");

    }

    /***
     * 修改信息
     * @param goods
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Result modify(@RequestBody Goods goods){
        try {
            //根据ID修改Goods信息
            int mcount = goodsService.updateGoodsById(goods);
            if(mcount>0){
                return new Result(true,"修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"修改失败");
    }



    /***
     * 根据ID查询Goods信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Goods getById(@PathVariable(value = "id")long id){
        //根据ID查询Goods信息
        Goods goods = goodsService.getOneById(id);
        return goods;
    }


    /***
     * 增加Goods数据
     * @param goods
     * 响应数据：success
     *                  true:成功  false：失败
     *           message
     *                  响应的消息
     *
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody Goods goods){
        try {
            //执行增加
            int acount = goodsService.add(goods);

            if(acount>0){
                //增加成功
               return new Result(true,"增加成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"增加失败");
    }



    /***
     * 分页查询数据
     * 获取JSON数据
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public PageInfo<Goods> list(@RequestBody Goods goods,@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return goodsService.getAll(goods,page, size);
    }



    /***
     * 查询所有
     * 获取JSON数据
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Goods> list() {
        return goodsService.getAll();
    }
}
