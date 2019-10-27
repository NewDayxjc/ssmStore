package com.pinyougou.seckill.service;
/**
 * @Author xiongjinchen
 * @Date 2019/10/7 13:43
 * @Version 1.0
 */

import org.springframework.security.core.Authentication;
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
 * @author : xiongjinchen
 * @description:
 * @date :2019/10/7 13:43
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authentications=new ArrayList<GrantedAuthority>();
        authentications.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authentications.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username,"",authentications);
    }
}
