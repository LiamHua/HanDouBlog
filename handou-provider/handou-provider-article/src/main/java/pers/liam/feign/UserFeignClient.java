package pers.liam.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liam
 * @date 2021/5/21 15:03
 */
@Service
@FeignClient(value = "handou-provider-user")
public interface UserFeignClient {
    /**
     * 根据昵称获取用户名
     * @param nickname
     * @return
     */
    @GetMapping("/user/getUsernameByNickname")
    String getUserNameByNickname(@RequestParam(value = "nickname") String nickname);
}
