package pers.liam.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pers.liam.entity.Article;
import pers.liam.entity.Category;
import pers.liam.feign.UserFeignClient;
import pers.liam.service.ArticleService;
import pers.liam.util.Res;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liam
 * @date 2021/4/24 23:12
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleServiceImpl;
    private final UserFeignClient userFeignClient;

    /**
     * 分页查询博客列表(不包含博客正文)
     * @param pageNo 页码
     * @param pageSize 每页数据量
     * @return
     */
    @GetMapping("/listArticle/{nickname}")
    public String getArticleList(@PathVariable String nickname, @RequestParam int pageNo, @RequestParam int pageSize) {
        if (pageNo < 1 || pageSize <1 || pageSize >100 || nickname == null || "".equals(nickname)) {
            return Res.failed();
        }

        String username = getUsername(nickname);
        if (username == null) {
            return Res.failed();
        }
        Page<Article> page = new Page<>(pageNo, pageSize);

        IPage<Article> articleList = articleServiceImpl.getAllArticleByUser(page, username, pageNo, pageSize);
        return Res.success(articleList);
    }

    /**
     * 根据id查询博客
     * @param id 博客id
     * @return
     */
    @GetMapping("/getArticle/{id}")
    public String getArticleById(@PathVariable int id) {
        Article article = articleServiceImpl.getById(id);

        if (article == null) {
            return Res.failed();
        }
        // 清除密码数据
        article.setSecret("");
        return Res.success(article);
    }

    /**
     * 新建文章
     * @param article
     * @param request
     * @return
     */
    @PostMapping("/addArticle")
    @Transactional(rollbackFor = Exception.class)
    public String addArticle(@RequestBody Article article, HttpServletRequest request) {
        String username = request.getHeader("username");
        // 修改文章时间
        article.setUpdateTime(Math.toIntExact(System.currentTimeMillis()/1000));
        // 插入文章
        boolean submitArticleFlag = articleServiceImpl.submitArticle(article);
        // 插入文章与分类的关系
        for (Category category : article.getCategory()) {
            boolean bindArticleCategoryFlag = articleServiceImpl.bindArticleCategory(article.getId(), category.getId());
            if (!bindArticleCategoryFlag) {
                return Res.failed();
            }
        }
        // 插入文章与用户的关系
        boolean bindArticleUserFlag = articleServiceImpl.bindArticleUser(article.getId(), username);
        if (!submitArticleFlag && !bindArticleUserFlag) {
            return Res.failed();
        }
       return Res.success();
    }

    /**
     * 根据昵称获取文章数量
     * @param nickname
     * @return
     */
    @GetMapping("/getArticleNum")
    public String getArticleNum(@RequestParam(value = "nickname") String nickname) {
        String username = getUsername(nickname);
        if (username == null) {
            return Res.failed();
        }
        int articleNum = articleServiceImpl.getArticleNum(username);
        return Res.success(articleNum);
    }

    /**
     * 根据昵称获取用户名
     * @param nickname
     * @return
     */
    public String getUsername(String nickname) {
        String str = userFeignClient.getUserNameByNickname(nickname);
        Res res = JSON.parseObject(str, Res.class);
        if (res.getStatus() != 1001) {
            return null;
        }
        return res.getData().toString();
    }

    /**
     * 搜索文章
     * @param name 文章名
     * @return
     */
    @GetMapping("/searchArticle")
    public String searchArticle(@RequestParam(value = "name") String name) {
        List<Article> articles = articleServiceImpl.searchArticle(name);
        if (articles == null) {
            return Res.failed();
        }
        return Res.success(articles);
    }
}
