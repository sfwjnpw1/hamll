package com.hmall.api.config;

import com.hmall.api.client.fallback.ItemClientFallbackFactory;
import com.hmall.api.client.fallback.PayClientFallbackFactory;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        /*OpenFeign只会在FeignClient所在包的日志级别为DEBUG时，才会输出日志。而且其日志级别有4级：
        - NONE：不记录任何日志信息，这是默认值。
        - BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
                - HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
                - FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。
        Feign默认的日志级别就是NONE，所以默认我们看不到请求日志。*/
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 获取登录用户
                Long userId = UserContext.getUser();
                if(userId == null) {
                    // 如果为空则直接跳过
                    return;
                }
                // 如果不为空则放入请求头中，传递给下游微服务
                template.header("user-info", userId.toString());
            }
        };
    }

    @Bean
    public ItemClientFallbackFactory itemClientFallback(){
        return new ItemClientFallbackFactory();
    }

    @Bean
    public PayClientFallbackFactory payClientFallbackFactory(){
        return new PayClientFallbackFactory();
    }
}