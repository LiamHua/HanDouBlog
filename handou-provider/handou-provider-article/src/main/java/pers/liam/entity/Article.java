package pers.liam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liam
 * @date 2021/5/17 17:44
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 168546532679583066L;

    /**
     * 博客表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 博客标题
     */
    @TableField(value = "article_title")
    private String articleTitle;

    /**
     * 博客内容
     */
    @TableField(value = "article_content")
    private String articleContent;

    /**
     * 博客摘要
     */
    @TableField(value = "article_abstract")
    private String articleAbstract;

    /**
     * 博客图片
     */
    @TableField(value = "article_img")
    private String articleImg;

    /**
     * 访问量
     */
    @TableField(value = "view_count")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @TableField(value = "star_count")
    private Integer starCount;

    /**
     * 收藏数
     */
    @TableField(value = "collect_count")
    private Integer collectCount;

    /**
     * 评论数
     */
    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Integer updateTime;

    /**
     * 文章状态
     */
    @TableField(value = "status")
    private int status;

    /**
     * 是否置顶
     */
    @TableField(value = "is_top")
    private Boolean isTop;

    /**
     * 是否开启评论
     */
    @TableField(value = "is_comment")
    private Boolean isComment;

    /**
     * 文章是否被加密
     */
    @TableField(value = "is_secret")
    private Boolean isSecret;

    /**
     * 文章密码
     */
    @TableField(value = "secret")
    private String secret;

    /**
     * 文章分类
     */
    @TableField(exist = false)
    private List<Category> category;
}