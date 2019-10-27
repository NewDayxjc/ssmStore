package com.pinyougou.mapper;

import com.pinyougou.model.Seller;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SellerMapper extends Mapper<Seller> {
    Seller findOne(@Param("username") String username);
}