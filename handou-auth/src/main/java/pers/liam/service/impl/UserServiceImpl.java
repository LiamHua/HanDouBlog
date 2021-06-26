package pers.liam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pers.liam.entity.UserDto;
import pers.liam.mapper.UserMapper;
import pers.liam.service.UserService;

/**
 * 用户登陆服务
 * @author liam
 * @date 2021/3/15 16:08
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDto> implements UserService {
    private final UserMapper userMapper;

    @Override
    public boolean register(UserDto userDto) {
        int insert = userMapper.insert(userDto);
        return insert == 1;
    }

    @Override
    public boolean saveUserRole(int userId, int roleId) {
        return userMapper.saveUserRole(userId, roleId);
    }
}
