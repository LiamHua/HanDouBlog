package pers.liam.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import pers.liam.entity.User;
import pers.liam.service.UserService;
import pers.liam.util.Res;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liam
 * @date 2021/5/20 23:00
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userServiceImpl;
    private final StringRedisTemplate tokenRedisTemplate;

    /**
     * 通过昵称获取用户信息
     * @param nickname 用户昵称
     * @return
     */
    @GetMapping("/getUserByNickname")
    public String getUserByNickname(@RequestParam(value = "nickname") String nickname) {
        User user = userServiceImpl.getUserByNickname(nickname);
        if (user == null) {
            return Res.failed();
        }
        return Res.success(user);
    }

    /**
     * 通过昵称获取用户名
     * @param nickname 用户昵称
     * @return
     */
    @GetMapping("/getUsernameByNickname")
    public String getUsernameByNickname(@RequestParam(value = "nickname") String nickname) {
        String username = userServiceImpl.getUsernameByNickname(nickname);
        if (username == null) {
            return Res.failed();
        }
        return Res.success(username);
    }

    /**
     * 通过id获取用户名
     * @param id 用户id
     * @return
     */
    @GetMapping("/getUsernameById")
    public String getUsernameById(@RequestParam(value = "id") int id) {
        String username = userServiceImpl.getUsernameById(id);
        if (username == null) {
            return Res.failed();
        }
        return Res.success(username);
    }

    @GetMapping("/getUserInfo")
    public String getUserInfo(HttpServletRequest request) {
        String username = request.getHeader("username");
        User userInfo = userServiceImpl.getUserInfo(username);
        if (userInfo == null) {
            return Res.failed();
        }
        return Res.success(userInfo);
    }

    /**
     * 退出登陆
     * @param request
     * @return
     */
    @PutMapping("/logout")
    public String logout(HttpServletRequest request) {
        String username = request.getHeader("username");
        Boolean deleteFlag = tokenRedisTemplate.delete(username);
        if ( deleteFlag == null || !deleteFlag) {
            return Res.failed();
        }
        return Res.success();
    }
}
