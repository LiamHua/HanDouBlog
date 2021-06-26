package pers.liam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liam
 * @date 2021/3/13 21:39
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    ///**
    // * 配置跨域
    // * @return
    // */
    //@Bean
    //public CorsWebFilter corsFilter() {
    //    CorsConfiguration config = new CorsConfiguration();
    //    // cookie跨域
    //    config.setAllowCredentials(Boolean.TRUE);
    //    config.addAllowedMethod("*");
    //    config.addAllowedOrigin("*");
    //    config.addAllowedHeader("*");
    //    // 配置前端js允许访问的自定义响应头
    //    //config.addExposedHeader("Authorization:");
    //
    //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
    //    source.registerCorsConfiguration("/**", config);
    //
    //    return new CorsWebFilter(source);
    //}

}
