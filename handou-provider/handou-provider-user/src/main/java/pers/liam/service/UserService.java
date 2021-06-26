package pers.liam.service;

import org.apache.ibatis.annotations.Param;
import pers.liam.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @author liam
 * @date 2021/05/20 22:59
 */
public interface UserService extends IService<User> {
    /**
     * 通过用户昵称获取部分用户信息
     * @param nickname 用户昵称
     * @return 用户信息
     */
    User getUserByNickname(@Param("nickname") String nickname);

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserInfo(@Param("username") String username);

    /**
     * 通过用户昵称获取用户名
     * @param nickname 用户昵称
     * @return 用户名
     */
    String getUsernameByNickname(@Param("nickname") String nickname);

    /**
     * 通过用户id获取用户名
     * @param id 用户id
     * @return 用户名
     */
    String getUsernameById(@Param("id") int id);
}
