package pers.liam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pers.liam.entity.UserDto;
import pers.liam.mapper.UserMapper;

/**
 * @author liam
 * @date 2021/4/13 17:41
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userMapper.getUser(username);
        if (user == null) {
            return null;
        }
        return User.withUsername(user.getUsername()).password(user.getPassword()).authorities("test").build();
    }
}
