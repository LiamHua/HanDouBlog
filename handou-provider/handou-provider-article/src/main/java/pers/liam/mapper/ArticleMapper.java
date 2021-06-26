package pers.liam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import pers.liam.entity.Article;

import java.util.List;

/**
 * @author liam
 * @date 2021/04/24 22:24
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 根据用户名查询博客列表（不查询正文）
     *
     * @param username 用户名
     * @return
     */
    IPage<Article> getAllArticleByUser(IPage<Article> page, @Param("username") String username, @Param("pageNo") int pageNo,
                                      @Param("pageSize") int pageSize);


    /**
     * 绑定文章与分类的关系
     *
     * @param articleId  文章id
     * @param categoryId 分类id
     * @return
     */
    boolean bindArticleCategory(@Param("articleId") int articleId, @Param("categoryId") int categoryId);

    /**
     * 绑定文章与用户的关系
     *
     * @param articleId 文章id
     * @param username  用户名
     * @return
     */
    boolean bindArticleUser(@Param("articleId") int articleId, @Param("username") String username);

    /**
     * 获取用户文章数量
     * @param username 用户名
     * @return
     */
    int getArticleNum(@Param("username") String username);

    /**
     * 文章搜索
     * @param name 文章名
     * @return
     */
    List<Article> searchArticle(@Param("name") String name);
}




