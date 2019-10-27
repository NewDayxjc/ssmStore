package com.pinyougou.cart.service;
/**
 * @Author xiongjinchen
 * @Date 2019/10/4 16:52
 * @Version 1.0
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录认证：CAS
 * 授权方法：SpringSecurity
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/4 16:52
 *
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username,"",authorities);
    }
}
