package pers.liam.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pers.liam.util.JwtUtil;
import pers.liam.util.Res;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 校验token是否合法
 *
 * @author liam
 * @date 2021/3/21 12:13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AccessFilter implements GlobalFilter, Ordered {
   private final StringRedisTemplate tokenRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json; charset=utf-8");
        String value = request.getPath().elements().get(1).value();
        if (!"auth".equals(value)) {
            RequestPath path = exchange.getRequest().getPath();
            log.info("正在访问" + path);
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            log.info("进入token过滤器，token为" + token);
            // 解析token
            Claims claims = JwtUtil.parseJwt(token);
            // 没有携带token || token有误
            if (token == null || "".equals(token) || claims == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBuffer buffer = response.bufferFactory().wrap(Res.failed().getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
            // 与redis中的token对比检测
            String username = (String)claims.get("username");

            String redisToken = tokenRedisTemplate.opsForValue().get(username);
            if (redisToken == null || !redisToken.equals(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBuffer buffer = response.bufferFactory().wrap(Res.failed().getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
            // 将用户名添加到请求头中
            request.mutate().header("username", username).build();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
