package com.springcasclient.service;
/**
 * @Author xiongjinchen
 * @Date 2019/10/3 18:54
 * @Version 1.0
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/3 18:54
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(username,"",authorities);
    }
}
