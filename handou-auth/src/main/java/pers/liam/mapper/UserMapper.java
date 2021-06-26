package pers.liam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.liam.entity.UserDto;

/**
 * User表数据库访问层
 *
 * @author liam
 * @date 2021/03/15 17:09
 */
@Repository
public interface UserMapper extends BaseMapper<UserDto> {
    /**
     * 查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserDto getUser(@Param("username") String username);

    /**
     * 指定用户身份
     * @param userId 用户id
     * @param roleId 身份id
     * @return
     */
    boolean saveUserRole(@Param("userId") int userId, @Param("roleId") int roleId);
}
