package pers.liam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import pers.liam.entity.Category;
import pers.liam.service.CategoryService;
import pers.liam.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * @author liam
 * @date 2021/5/17 17:44
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    private final CategoryMapper categoryMapper;

    @Override
    public int getCategoryNum(String username) {
        return categoryMapper.getCategoryNum(username);
    }
}




