package com.hmall.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class MqConfig {
    @Bean
    public MessageConverter messageConverter(){
        /*// 1.定义消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;*/
        // 同上优化为一行
        // return new Jackson2JsonMessageConverter();

        // 消息传递用户信息
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(){

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                Long userId= message.getMessageProperties().getHeader("user-info");
                if(ObjectUtil.isNotNull(userId)){
                    UserContext.setUser(userId);
                }
                return super.fromMessage(message);
            }
        };
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }
}
