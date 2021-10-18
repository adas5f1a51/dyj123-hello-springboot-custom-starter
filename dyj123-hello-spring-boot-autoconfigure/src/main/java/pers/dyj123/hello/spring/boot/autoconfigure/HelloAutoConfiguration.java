package pers.dyj123.hello.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.dyj123.hello.service.HelloService;
import pers.dyj123.hello.service.impl.HelloServiceImpl;

/**
 * @author dyj123
 * @project springboot-custom-starter
 * @create 2021/10/18 9:44
 * @description 自动配置类：Hello
 */
@Configuration
@EnableConfigurationProperties(HelloProperties.class)
@ConditionalOnMissingBean(HelloAutoConfiguration.class)
public class HelloAutoConfiguration {

    private final HelloProperties properties;

    public HelloAutoConfiguration(HelloProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService() {
        return new HelloServiceImpl(properties.getMessage());
    }

}
