package com.pinyougou.sellergoods.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.model.Brand;
import com.pinyougou.model.SpecificationOption;
import com.pinyougou.model.TypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;
    @Autowired
    private RedisTemplate redisTemplate;

	/**
	 * 返回TypeTemplate全部列表
	 * @return
	 */
	@Override
    public List<TypeTemplate> getAll(){
        return typeTemplateMapper.selectAll();
    }


    /***
     * 分页返回TypeTemplate列表
     * @param pageNum
     * @param pageSize
     * @return
     */
	@Override
    public PageInfo<TypeTemplate> getAll(TypeTemplate typeTemplate,int pageNum, int pageSize) {
        //执行分页
        PageHelper.startPage(pageNum,pageSize);
       
        //执行查询
        List<TypeTemplate> all = typeTemplateMapper.select(typeTemplate);
        PageInfo<TypeTemplate> pageInfo = new PageInfo<TypeTemplate>(all);
        //执行更新缓存
        modifyTemplate();
        return pageInfo;
    }

    public void modifyTemplate(){
        List<TypeTemplate> typeTemplates = typeTemplateMapper.selectAll();

        for (TypeTemplate template : typeTemplates) {
            //从模板获取品牌数据

            List<Map> brandList=JSON.parseArray(template.getBrandIds(),Map.class);
            //存储在redis中
            redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);
            //获取规格
//            [
//            {"id":1,"text":"网络制式","options":[{"id":123,"optionName":"联通2G"}]},
//            {"id":2,"text":"机身内存","options":[{"id":1233,"optionName":"4G","8G"}]},
//            {"id":3,"text":"操作系统","options":[{"id":1234,"optionName":"Android","IOS"}]}
//]
            List<Map> attributeList = JSON.parseArray(template.getCustomAttributeItems(), Map.class);
            //调用之前的getOptionByTypeId方法获取规格数据
            List<Map> specList = getOptionByTypeId(template.getId());
            redisTemplate.boundHashOps("specList").put(template.getId(),specList);
        }
    }



    /***
     * 增加TypeTemplate信息
     * @param typeTemplate
     * @return
     */
    @Override
    public int add(TypeTemplate typeTemplate) {
        return typeTemplateMapper.insertSelective(typeTemplate);
    }


    /***
     * 根据ID查询TypeTemplate信息
     * @param id
     * @return
     */
    @Override
    public TypeTemplate getOneById(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }


    /***
     * 根据ID修改TypeTemplate信息
     * @param typeTemplate
     * @return
     */
    @Override
    public int updateTypeTemplateById(TypeTemplate typeTemplate) {
        return typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
    }


    /***
     * 根据ID批量删除TypeTemplate信息
     * @param ids
     * @return
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        //创建Example，来构建根据ID删除数据
        Example example = new Example(TypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();

        //所需的SQL语句类似 delete from tb_typeTemplate where id in(1,2,5,6)
        criteria.andIn("id",ids);
        return typeTemplateMapper.deleteByExample(example);
    }

    @Override
    public List<Map> getOptionByTypeId(long id) {
        //先查询出模板中的规格信息，[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        //将spec_id转成JSON,并循环   这里转换为Map集合，是因为这里没有用到List集合
        List<Map> dataMap= JSON.parseArray(typeTemplate.getSpecIds(),Map.class);  //TODO 模板id未取到
        for (Map map: dataMap) {
            long specId = Long.parseLong(map.get("id").toString());
            //根据spec_id的JSON值中id值去数据(tb_specification_option)库查询规格选项
            SpecificationOption specificationOption=new SpecificationOption();
            specificationOption.setSpecId(specId);
            List<SpecificationOption> options = specificationOptionMapper.select(specificationOption);
            //组装JSON数据格式
            map.put("options",options);
        }
        return  dataMap;
    }
}
