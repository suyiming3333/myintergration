package com.corn.myintergration.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: suyiming
 * @Date: 18-11-16 23:44
 * @Description:
 */

@Configuration
public class RabbitConfig {

    @Bean
    public Queue Queue(){
        return new Queue("hello");
    }
}
