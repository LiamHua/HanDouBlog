package pers.liam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liam.entity.UserDto;

/**
 * @author liam
 * @date 2021/4/13 17:35
 */
public interface UserService extends IService<UserDto> {
    /**
     * 用户注册
     * @param userDto 用户
     * @return
     */
    boolean register(UserDto userDto);

    boolean saveUserRole(int userId, int roleId);
}
