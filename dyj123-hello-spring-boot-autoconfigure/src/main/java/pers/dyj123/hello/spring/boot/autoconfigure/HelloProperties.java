package pers.dyj123.hello.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dyj123
 * @project springboot-custom-starter
 * @create 2021/10/18 9:51
 * @description 配置参数类：Hello
 */
@ConfigurationProperties(prefix = HelloProperties.HELLO_PREFIX)
public class HelloProperties {

    public static final String HELLO_PREFIX = "hello";

    /**
     * 欢迎信息(Hello message)
     *
     * default value: `Hello World!`
     */
    private String message = "Hello World!";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
