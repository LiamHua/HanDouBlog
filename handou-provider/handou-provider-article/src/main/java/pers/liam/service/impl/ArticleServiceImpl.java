package pers.liam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pers.liam.entity.Article;
import pers.liam.mapper.ArticleMapper;
import pers.liam.service.ArticleService;

import java.util.List;

/**
 * @author liam
 * @date 2021/04/24 22:24
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{
    private final ArticleMapper articleMapper;

    @Override
    public IPage<Article> getAllArticleByUser(IPage<Article> page, String username, int pageNo, int pageSize) {
        return articleMapper.getAllArticleByUser(page, username, pageNo, pageSize);
    }

    @Override
    public boolean submitArticle(Article article) {
        int insert = articleMapper.insert(article);
        return insert == 1;
    }

    @Override
    public boolean bindArticleCategory(int articleId, int categoryId) {
        return articleMapper.bindArticleCategory(articleId, categoryId);
    }

    @Override
    public boolean bindArticleUser(int articleId, String username) {
        return articleMapper.bindArticleUser(articleId, username);
    }

    @Override
    public int getArticleNum(String username) {
        return articleMapper.getArticleNum(username);
    }

    @Override
    public List<Article> searchArticle(String name) {
        return articleMapper.searchArticle(name);
    }
}




