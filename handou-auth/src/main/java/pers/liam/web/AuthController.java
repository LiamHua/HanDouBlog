package pers.liam.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.cloud.spring.boot.sms.ISmsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import pers.liam.entity.UserDto;
import pers.liam.enums.ResponseStatus;
import pers.liam.feign.ArticleFeignClient;
import pers.liam.service.UserService;
import pers.liam.util.JwtUtil;
import pers.liam.util.RegexUtil;
import pers.liam.util.Res;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 用户登陆与注册
 * @author liam
 * @date 2021/3/13 14:50
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userServiceImpl;
    private final UserDetailsService userDetailsServiceImpl;
    private final ArticleFeignClient articleFeignClient;
    private final StringRedisTemplate tokenRedisTemplate;
    private final ISmsService smsService;
    private final StringRedisTemplate smsRedisTemplate;

    @Value("${alibaba.sms.sign-name}")
    private String signName;
    @Value("${alibaba.sms.template-code}")
    private String templateCode;

    /**
     * 密码登陆
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if (!RegexUtil.isTel(username)) {
            return Res.failed();
        }
        log.info(username + "正在登陆");
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        // 用户不存在
        if (userDetails == null) {
            return Res.failed(ResponseStatus.NOT_EXIST_USERNAME);
        }
        // 密码错误
        if (!password.equals(userDetails.getPassword())) {
            return Res.failed(ResponseStatus.PASSWORD_ERROR);
        }
        // 登陆成功
        log.info(username + "登陆成功");
        HashMap<String, String> map = new HashMap<>(4);
        map.put("username", userDetails.getUsername());
        // 生成token
        String token = JwtUtil.createJwt(map);
        // 将token存入redis中
        tokenRedisTemplate.opsForValue().set(username, token, 7, TimeUnit.DAYS);
        // 将token返回给前端
        HashMap<String, String> tokenMap = new HashMap<>(4);
        tokenMap.put("token", token);
        return Res.success(tokenMap);
    }

    /**
     * 验证码登陆
     * @param loginForm 表单数据
     * @return
     */
    @PostMapping("/loginWithSms")
    public String loginWithSms(@RequestBody HashMap<String,String> loginForm) {
        String username = loginForm.get("username");
        String code = loginForm.get("smsCode");
        // 校验手机号及验证码长度
        if (!RegexUtil.isTel(username) || code.length() != 6) {
            return Res.failed();
        }
        // 校验验证码
        String smsCode = smsRedisTemplate.opsForValue().get(username);
        if (!code.equals(smsCode)) {
            return Res.failed(ResponseStatus.SMS_CODE_ERROR);
        }
        // 获取用户信息
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        // 用户不存在
        if (userDetails == null) {
            // 注册
            boolean flag = register(username);
            if (!flag) {
                log.info(username + "注册失败");
                return Res.failed();
            }
            log.info(username + "注册成功");
        }
        // 再次获取用户信息
        userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        // 登陆成功
        log.info(username + "登陆成功");
        // 验证码只能被使用一次
        smsRedisTemplate.delete(username);
        HashMap<String, String> map = new HashMap<>(4);
        map.put("username", userDetails.getUsername());
        // 生成token
        String token = JwtUtil.createJwt(map);
        // 将token存入redis中,有效期7天
        tokenRedisTemplate.opsForValue().set(username, token, 7, TimeUnit.DAYS);
        // 将token返回给前端
        HashMap<String, String> tokenMap = new HashMap<>(4);
        tokenMap.put("token", token);
        return Res.success(tokenMap);
    }

    /**
     * 获取短信验证码
     * @param username 手机号（即用户名）
     * @return
     */
    @GetMapping("/getSMS/{username}")
    public String getSMS(@PathVariable String username) {
        if (!RegexUtil.isTel(username)) {
            return Res.failed();
        }
        // 产生6位随机验证码
        String code = String.valueOf(RandomUtil.randomInt(111111,999999));
        // 发送验证码请求
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setPhoneNumbers(username);
        smsRequest.setSignName(signName);
        smsRequest.setTemplateCode(templateCode);
        smsRequest.setTemplateParam("{\"code\": \"" + code + "\"}");
        try {
            SendSmsResponse smsResponse = smsService.sendSmsRequest(smsRequest);
            // 验证码发送成功
            if ("OK".equals(smsResponse.getCode())) {
                // 将验证码存入redis，有效期5分钟
                smsRedisTemplate.opsForValue().set(username, code,5,TimeUnit.MINUTES);
                return Res.success();
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return Res.failed();
    }

    // TODO 这里可能会出现问题 1.事务没有回滚 2.feign调用出现错误
    /**
     * 用户注册
     * @param username 用户名
     * @return 是否注册成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean register(String username) {
        try {
            // 用户初始化
            UserDto user = new UserDto();
            user.setUsername(username);
            user.setNickname(username);
            user.setSex("男");
            user.setCreateTime(Math.toIntExact(System.currentTimeMillis() / 1000));
            user.setUpdateTime(Math.toIntExact(System.currentTimeMillis() / 1000));
            user.setHeadUrl("https://bean.huazai.vip/cover-image/19608507998243549438864915840260-blog.png");

            boolean flag = userServiceImpl.register(user);
            if (!flag) {
                return false;
            }
            // 添加用户角色信息
            int id = user.getId();
            boolean saveUserRole = userServiceImpl.saveUserRole(id, 1);
            if (!saveUserRole) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            // 增加用户博客默认分类
            HashMap<String, String> map = new HashMap<>(4);
            map.put("username", username);
            map.put("name", "默认分类");
            String jsonString = JSON.toJSONString(map);
            String result = articleFeignClient.addDefaultCategory(jsonString);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("status") != 200) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }
}
