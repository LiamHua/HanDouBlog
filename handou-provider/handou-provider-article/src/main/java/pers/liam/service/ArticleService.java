package pers.liam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import pers.liam.entity.Article;

import java.util.List;

/**
 * @author liam
 * @date 2021/04/24 22:24
 */
public interface ArticleService extends IService<Article> {
    /**
     * 根据用户名查询博客列表（不查询正文）
     *
     * @param username 用户名
     * @param pageNo 页数
     * @param pageSize 每页大小
     * @return
     */
    IPage<Article> getAllArticleByUser(IPage<Article> page, @Param("username") String username, @Param("pageNo") int pageNo,
                                       @Param("pageSize") int pageSize);

    /**
     * 提交文章
     *
     * @param article
     * @return
     */
    boolean submitArticle(Article article);

    /**
     * 绑定文章与分类的关系
     *
     * @param articleId  文章id
     * @param categoryId 分类id
     * @return
     */
    boolean bindArticleCategory(int articleId, int categoryId);

    /**
     * 绑定文章与用户的关系
     *
     * @param articleId 文章id
     * @param username  用户名
     * @return
     */
    boolean bindArticleUser(int articleId, String username);

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
    List<Article> searchArticle(String name);
}
