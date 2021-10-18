package pers.dyj123.hello.service.impl;

import pers.dyj123.hello.service.HelloService;

/**
 * @author dyj123
 * @project springboot-custom-starter
 * @create 2021/10/18 9:39
 * @description 业务层接口实现类：Hello
 */
public class HelloServiceImpl implements HelloService {

    private final String message;

    public HelloServiceImpl(String message) {
        this.message = message;
    }

    /**
     * 输出欢迎信息
     *
     * @return 欢迎信息
     */
    @Override
    public String sayHello() {
        return message;
    }

}
