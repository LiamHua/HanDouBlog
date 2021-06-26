package pers.liam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import pers.liam.entity.User;
import pers.liam.service.UserService;
import pers.liam.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author liam
 * @date 2021/05/20 22:59
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;

    @Override
    public User getUserByNickname(String nickname) {
        return userMapper.getUserByNickname(nickname);
    }

    @Override
    public User getUserInfo(String username) {
        return userMapper.getUserInfo(username);
    }

    @Override
    public String getUsernameByNickname(String nickname) {
        return userMapper.getUsernameByNickname(nickname);
    }

    @Override
    public String getUsernameById(int id) {
        return userMapper.getUsernameById(id);
    }
}




