package pers.liam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义状态码
 * @author liam
 * @date 2021/4/13 15:05
 */
@Getter
@AllArgsConstructor
public enum ResponseStatus {
    // 操作成功
    SUCCESS(1001,"操作成功"),
    // 操作失败
    FAILED(1002, "操作失败"),
    // 用户已存在
    EXIST_USERNAME(1003, "该用户已存在"),
    // 用户不存在
    NOT_EXIST_USERNAME(1004, "该用户不存在"),
    // 密码错误
    PASSWORD_ERROR(1005, "密码错误"),
    // 用户状态异常
    USER_STATE_ERROR(1006, "用户状态异常，请联系管理员"),
    // 新密码不能与原密码一致
    SAME_PASSWORD_ERROR(1007, "新密码不能与原密码一致"),
    // 无权访问
    UNAUTHORIZATION(1008, "无权访问"),
    // 验证码错误
    SMS_CODE_ERROR(1009, "验证码错误");

    private final int status;
    private final String msg;
}
