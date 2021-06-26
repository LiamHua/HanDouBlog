package pers.liam.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pers.liam.entity.Category;
import pers.liam.feign.UserFeignClient;
import pers.liam.service.CategoryService;
import pers.liam.util.Res;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liam
 * @date 2021/5/17 17:44
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryServiceImpl;
    private final UserFeignClient userFeignClient;

    /**
     * 新增分类
     * @param category
     * @param request
     * @return
     */
    @PostMapping("/addCategory")
    public String addCategory(@RequestBody Category category, HttpServletRequest request) {
        // 分类名称的长度在1-10之间
        if (category.getName().length() < 1 || category.getName().length() > 10) {
            return Res.failed();
        }
        String username = request.getHeader("username");
        if (username != null) {
            category.setUsername(username);
        }
        boolean duplicate = checkCategoryDuplicate(username, category.getName());
        if (duplicate) {
            return Res.failed();
        }
        category.setCreateTime(Math.toIntExact(System.currentTimeMillis() / 1000));
        category.setUpdateTime(Math.toIntExact(System.currentTimeMillis() / 1000));
        boolean flag = categoryServiceImpl.save(category);
        if (!flag) {
            return Res.failed();
        }
        return Res.success();
    }

    /**
     * 获取分类列表
     * @param request
     * @return
     */
    @GetMapping("/getCategoryList")
    public String getCategoryList(HttpServletRequest request) {
        String username = request.getHeader("username");
        List<Category> categoryList = categoryServiceImpl.list(Wrappers.<Category>query().eq("username", username)
        .select("id","name"));
        if (categoryList == null || categoryList.size() == 0) {
            return Res.failed();
        }
        return Res.success(categoryList);
    }

    /**
     * 检测分类名称是否重复
     * @param username 用户名
     * @param name 分类名称
     * @return
     */
    private boolean checkCategoryDuplicate(String username, String name) {
        Category category = categoryServiceImpl.getOne(Wrappers.<Category>query().eq("name", name)
                .eq("username", username));
        return category != null;
    }

    /**
     * 根据昵称获取分类数量
     * @param nickname 昵称
     * @return
     */
    @GetMapping("/getCategoryNum")
    public String getArticleNum(@RequestParam(value = "nickname") String nickname) {
        String username = getUsername(nickname);
        if (username == null) {
            return Res.failed();
        }
        int categoryNum = categoryServiceImpl.getCategoryNum(username);
        return Res.success(categoryNum);
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
}
