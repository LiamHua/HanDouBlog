package pers.liam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liam
 * @date 2021/05/20 22:59
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 用户表自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名（手机号）
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField(value = "head_url")
    private String headUrl;

    /**
     * 用户封面图
     */
    @TableField(value = "back_url")
    private String backUrl;

    /**
     * 用户介绍
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 用户性别
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 用户职称
     */
    @TableField(value = "job")
    private String job;

    /**
     * 用户地点
     */
    @TableField(value = "location")
    private String location;

    /**
     * 用户创建时间
     */
    @TableField(value = "create_time")
    private Integer createTime;

    /**
     * 用户修改时间
     */
    @TableField(value = "update_time")
    private Integer updateTime;

    /**
     * 用户状态
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = -2688112926785128763L;
}