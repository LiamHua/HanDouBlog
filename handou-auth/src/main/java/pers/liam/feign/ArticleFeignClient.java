package pers.liam.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author liam
 * @date 2021/5/18 00:12
 */
@Service
@FeignClient(value = "handou-provider-article")
public interface ArticleFeignClient {
    /**
     * 调用Article模块
     * @param json
     * @return
     */
    @PostMapping(value = "/category/addCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
    String addDefaultCategory(String json);
}
