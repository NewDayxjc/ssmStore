package com.pinyougou.sellergoods.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.model.*;
import com.pinyougou.sellergoods.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
//添加声明式注解
@Transactional
public class GoodsServiceImpl implements GoodsService {
//    private ItemCat itemCat;
//    private Date now;
//    private Brand brand;
//    private Seller seller;

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpecificationMapper specMapper;
    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;//查询分类
    @Autowired
    private ItemMapper itemMapper;
	/**
	 * 返回Goods全部列表
	 * @return
	 */
	@Override
    public List<Goods> getAll(){

        return goodsMapper.selectAll();
    }


    /***
     * 分页返回Goods列表
     * @param pageNum
     * @param pageSize
     * @return
     */
	@Override
    public PageInfo<Goods> getAll(Goods goods,int pageNum, int pageSize) {
        //执行分页
        PageHelper.startPage(pageNum,pageSize);
        //执行查询
//        List<Goods> all = goodsMapper.select(goods);
        Example example=new Example(Goods.class);
        Example.Criteria criteria=example.createCriteria();
        if(goods!=null){
            //商家sellerId查询
            if(StringUtils.isNotBlank(goods.getSellerId())){
            criteria.andEqualTo("sellerId",goods.getSellerId());
        }
        //状态查询
        if(StringUtils.isNotBlank(goods.getAuditStatus())){
            criteria.andEqualTo("auditStatus",goods.getAuditStatus());
        }
        //模糊查询  select * from tb_goods where sellerId=? anditStatus=? and goodsName like '%小红%'
        if(StringUtils.isNotBlank(goods.getGoodsName())){
            criteria.andEqualTo("goodsName","%"+goods.getGoodsName()+"%");
        }

     }
        //isDelete=null  表示没有删除数据
        //删除时不是真正的物理删除11

            criteria.andIsNull("isDelete");

     //查询
        List<Goods> all = goodsMapper.selectByExample(example);

        PageInfo<Goods> pageInfo = new PageInfo<Goods>(all);
        return pageInfo;
    }



    /***
     * 增加Goods信息
     * @param goods
     * @return
     */
    @Override
    public int add(Goods goods) {
        //默认状态
        goods.setAuditStatus("0");
        //新增商品
        int account = goodsMapper.insertSelective(goods);
        //商品详情
        goods.getGoodsDesc().setGoodsId(goods.getId());//设置主键为商品主键
        goodsDescMapper.insertSelective(goods.getGoodsDesc());
        addGoods(goods);
//        buildGoods(goods);

        return account;
    }
//增加商品item
    public void addGoods(Goods goods) {
        //当前时间，放在循环外部
        Date now=new Date();
        //category
        ItemCat itemCat= itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
        //brand
        Brand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
        //seller
        Seller seller = sellerMapper.selectByPrimaryKey(goods.getSellerId());

//如果启用了规格，则批量增加SKU item
        if(goods.getIsEnableSpec().equals("1")){
            //增加sku
            for(Item items:goods.getItems()){
                //标题 华为荣耀4 16G 联通3G
                String title=" ";
                //获取goods的名称
                String goodsName=goods.getGoodsName();
                title=title+goodsName;
                //规格属性 {"机身内存":"16G","网络":"联通3G"} 键值对形式
                Map<String,String> specMap= JSON.parseObject(items.getSpec(),Map.class);//将spec转换为map
                //遍历map集合  map.entrySet().for
                for (Map.Entry<String, String> entry : specMap.entrySet()) {
                    title+=" "+entry.getValue().toString();
                    items.setTitle(title);
                }
                goodsParameterInit(goods, now, itemCat, brand, seller, items);


                //将item添加进数据库
                itemMapper.insertSelective(items);

            }

        }else{
            //增加sku
            Item items=new Item();

                //获取goods的名称
                String goodsName=goods.getGoodsName();
                    items.setTitle(goodsName);
            //存储图片
            //红色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOWs2ABppQAAETwD7A1Is142.jpg"}]
            goodsParameterInit(goods, now, itemCat, brand, seller, items);
            //价格
            items.setPrice(goods.getPrice());
            //库存
            items.setNum(1);
            //是否启用
            items.setStatus("1");
            //默认状态
            items.setIsDefault("1");
            //将item添加进数据库
            itemMapper.insert(items);
            }
    }
    //构建商品item，以集合的形式在xml添加，提升效率
    public List<Item> buildGoods(Goods goods) {
        List<Item> itemList=new ArrayList<Item>();
        //当前时间，放在循环外部
        Date now=new Date();
        //category
        ItemCat itemCat= itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
        //brand
        Brand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
        //seller
        Seller seller = sellerMapper.selectByPrimaryKey(goods.getSellerId());

//如果启用了规格，则批量增加SKU item
        if(goods.getIsEnableSpec().equals("1")){
            //增加sku
            for(Item items:goods.getItems()){
                //标题 华为荣耀4 16G 联通3G
                String title=" ";
                //获取goods的名称
                String goodsName=goods.getGoodsName();
                title=title+goodsName;
                //规格属性 {"机身内存":"16G","网络":"联通3G"} 键值对形式
                Map<String,String> specMap= JSON.parseObject(items.getSpec(),Map.class);//将spec转换为map
                //遍历map集合  map.entrySet().for
                for (Map.Entry<String, String> entry : specMap.entrySet()) {
                    title+=" "+entry.getValue().toString();
                    items.setTitle(title);
                }
                goodsParameterInit(goods, now, itemCat, brand, seller, items);


                //将item添加进数据库
//                itemMapper.insertSelective(items);
                itemList.add(items);
            }

        }else{
            //增加sku
            Item items=new Item();

            //获取goods的名称
            String goodsName=goods.getGoodsName();
            items.setTitle(goodsName);
            //存储图片
            //红色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOWs2ABppQAAETwD7A1Is142.jpg"}]
            goodsParameterInit(goods, now, itemCat, brand, seller, items);
            //价格
            items.setPrice(goods.getPrice());
            //库存
            items.setNum(1);
            //是否启用
            items.setStatus("1");
            //默认状态
            items.setIsDefault("1");
            //将item添加进数据库
//            itemMapper.insert(items);
            itemList.add(items);
        }
        return itemList;

    }
//抽取
    public void goodsParameterInit(Goods goods, Date now, ItemCat itemCat, Brand brand, Seller seller, Item items) {
        //存储图片
        //红色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOWs2ABppQAAETwD7A1Is142.jpg"}]
        String goodsDescItemImages = goods.getGoodsDesc().getItemImages();//键值对形式的集合
        List<Map> imagesMap = JSON.parseArray(goodsDescItemImages, Map.class);
        String url = imagesMap.get(0).get("url").toString();
        items.setImage(url);

        //分类ID
        items.setCategoryid(goods.getCategory3Id());
        //创建时间 修改时间
        items.setCreateTime(now);
        items.setUpdateTime(now);
        //设置SKU也就是goods的id
        items.setGoodsId(goods.getId());
        //设置sellerID
        items.setSellerId(goods.getSellerId());
        //category

        items.setCategory(itemCat.getName());
//这里只显示名字，不想再关联表，叫数据冗余
        //brand

        items.setBrand(brand.getName());
        //spec
//            Specification specification = specMapper.selectByPrimaryKey(goods.getId());
//            items.setSpec(specification.getSpecName());
        //seller

        items.setSeller(seller.getName());
        //spec
//            Specification specification = specMapper.selectByPrimaryKey(goods.getId());
//            items.setSpec(specification.getSpecName());
    }



    /***
     * 根据ID查询Goods信息
     * @param id
     * @return
     */
    @Override
    public Goods getOneById(Long id) {
        Goods goods=goodsMapper.selectByPrimaryKey(id);
        GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(goodsDesc);
        //select * from tb_item where goodsId=?
        Item item=new Item();
        item.setGoodsId(id);
        List<Item> items = itemMapper.select(item);
        goods.setItems(items);
        return goods;
    }


    /***
     * 根据ID修改Goods信息
     * @param goods
     * @return
     */
    @Override
    public int updateGoodsById(Goods goods) {
        //在serviceImpl进行商家是否为该商品上架  update goods set ? where sellerId=goods.getSellerId

//修改后设置状态由审核通过为待审核

            goods.setAuditStatus("0");
            //修改goods
            int acount = goodsMapper.updateByPrimaryKeySelective(goods);
            //修改goodsDesc
//        GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goods.getId());
//        goods.setGoodsDesc(goodsDesc);
            goodsDescMapper.updateByPrimaryKeySelective(goods.getGoodsDesc());
//   测试注解     int i=19/0;
        /*
        修改item
        * */
            //先删除item  select * from tb_item where goodsId=?;
        try { Item item = new Item();
            item.setGoodsId(goods.getId());
            itemMapper.delete(item);
            //新增item  for循环增加-批量增加-xml foreach效率高
            //goods.getItems();    ????
//        addGoods(goods);
            //for循环增加-批量增加-xml foreach   批量增加
            List<Item> items = buildGoods(goods);
            itemMapper.batchInsert(items);

        }catch (Exception e){
            e.printStackTrace();
        }
        return acount;
    }


    /***
     * 根据ID批量删除Goods信息
     * @param ids
     * @return
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        //创建Example，来构建根据ID删除数据
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();

        //所需的SQL语句类似 delete from tb_goods where id in(1,2,5,6)
        criteria.andIn("id",ids);
        //7)删除-一般为了保证数据的一致性，一般不做物理删除,为了查询历史数据，也不会做删除
        //	is_delete  非1没有删除
        //		   1 表示已删除  update tb_goods set is_delete="1" wherer id in(X,X,X,X)
//        return goodsMapper.deleteByExample(example);
        Goods goods=new Goods();
        goods.setIsDelete("1");
        int acount=goodsMapper.updateByExampleSelective(goods,example);
        return acount;
    }

    @Override
    public int updateStatus(List<Long> ids, String status) {
        //根据ids批量修改状态
        Example example=new Example(Goods.class);
        Example.Criteria criteria=example.createCriteria();
        //update tb_goods set ? where id in(X,X,X,X,X);
        //criteria.andIn("实体类的参数",ids);
        criteria.andIn("id",ids);
        //设置状态
        Goods goods=new Goods();
        goods.setAuditStatus(status);
        int amount = goodsMapper.updateByExampleSelective(goods, example);
        return amount;
    }

    /**
     * 根据多个goodsId查询Items
     * @param ids
     * @param status
     * @return
     */
    @Override
    public List<Item> getItemByIdAndStatus(List<Long> ids, String status) {
        //构建查询条件
        Example example=new Example(Item.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andIn("goodsId",ids);
        criteria.andEqualTo("status",status);
        return itemMapper.selectByExample(example);
    }


}
