package pers.dyj123.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pers.dyj123.hello.service.HelloService;

/**
 * @author dyj123
 * @project springboot-custom-starter
 * @create 2021/10/18 9:34
 * @description 控制器：Hello
 */
@RestController
public class HelloController {

    private HelloService service;

    @Autowired
    public void setService(HelloService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return service.sayHello();
    }

}
