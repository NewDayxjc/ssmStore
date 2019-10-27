package com.pinyougou.user.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.model.User;

import java.util.List;

public interface UserService {
    User getUserInfoByUserName(String username);
    /***
     * 增加User信息
     * @param user
     * @return
     */
    int add(User user);

    /**
     * 根据phone发送验证码
     * @param phone
     * @throws Exception
     */
    void createCode(String phone) throws Exception;

    /**
     * 根据username查找用户个数
     * @param username
     * @return
     */
    int findByUserName(String username);

    /**
     * 审核验证码
     * @param phone
     * @param code
     * @return
     */
    Boolean check(String phone, String code);

    /**
     * 根据phone查找用户个数
     * @param phone
     * @return
     */
    int findByPhone(String phone);
}
