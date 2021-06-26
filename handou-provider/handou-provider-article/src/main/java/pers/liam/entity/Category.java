package pers.liam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liam
 * @date 2021/5/17 17:44
 */
@TableName(value ="category")
@Data
public class Category implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "username")
    private String username;

    /**
     * 分类名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 分类创建时间
     */
    @TableField(value = "create_time")
    private Integer createTime;

    /**
     * 分类修改时间
     */
    @TableField(value = "update_time")
    private Integer updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}