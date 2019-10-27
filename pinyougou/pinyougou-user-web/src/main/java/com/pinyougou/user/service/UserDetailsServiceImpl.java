package com.pinyougou.user.service;
/**
 * @Author xiongjinchen
 * @Date 2019/10/3 20:44
 * @Version 1.0
 */

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/3 20:44
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private UserService userService;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.pinyougou.model.User user = userService.getUserInfoByUserName(username);
        List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(user.getUsername(),user.getPassword(),authorities);
    }
}
