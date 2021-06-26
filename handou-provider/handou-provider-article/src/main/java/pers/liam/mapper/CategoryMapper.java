package pers.liam.mapper;

import org.apache.ibatis.annotations.Param;
import pers.liam.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author liam
 * @date 2021/5/17 17:44
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 获取用户分类数量
     * @param username 用户名
     * @return
     */
    int getCategoryNum(@Param("username") String username);
}




