# SpringBoot - 自定义模块 starter

本节学习自定义SpringBoot模块starter的步骤和注意事项。

## 1. 为什么要自定义模块 starter

传统方式使用 Spring 开发项目，需要编写一大堆配置文件，引入很多依赖。久而久之配置文件维护难度加大，依赖引入难于控制（被戏称为“依赖地狱”），加重了开发人员的负担。

为此 Spring 推出了 Spring Boot 用来简化开发，Spring Boot 的核心就是使用各种模块的 starter（又称启动器）来进行配置参数，依赖导入等功能的自动化。

> Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".<br>
> [Spring Boot - spring.io](https://spring.io/projects/spring-boot#overview)

在开发过程中，对于某些常用的代码或功能，我们可以抽取出来，编写我们自定义模块的 starter 来实现这些功能，帮助我们使得开发更简单更有效率。

## 2. 简述模块 starter 原理

starter 启动器包**只是负责引入依赖**，并没有实际代码。真正实现自动配置功能的是 autoconfigure **自动配置包**以及**我们需要抽取的代码**。

细节：

* starter 启动器包负责引入 autoconfigure 自动配置包和我们需要抽取的代码包；
* autoconfigure 自动配置包编写自动配置类和配置参数类，并引入相关依赖；
* autoconfigure 自动配置包需要编写`spring.factories`配置文件，以让Spring读取并使用自定义 starter 。

## 3. 编写自定义模块 starter

需要编写以下包：

* 我们需要抽取的代码包；
* autoconfigure 自动配置包；
* starter 启动器包。

### 3.1 代码包

代码包负责将我们需要抽取的代码封装好。

我以**访问一个请求能接收一段文字**这个功能编写代码。

控制器：

```java
@RestController
public class HelloController {
    private HelloService service;
    @Autowired // 自动注入
    public void setService(HelloService service) {
        this.service = service;
    }
    @GetMapping("/hello")
    public String sayHello() {
        return service.sayHello();
    }
}
```

业务逻辑：

```java
// 通过自动配置类注册到容器
public class HelloServiceImpl implements HelloService {
    private final String message;
    // 通过构造器注入
    public HelloServiceImpl(String message) {
        this.message = message;
    }
    @Override
    public String sayHello() {
        return message;
    }
}
```

依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>5.3.6</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
        <version>2.4.5</version>
    </dependency>
</dependencies>
```

### 3.2 autoconfigure 自动配置包

autoconfigure 自动配置包负责代码包的自动配置功能以及装配功能

* 自动配置包命名规则：`<项目/模块名>-spring-boot-autoconfigure`； 
* 包名命名规则：`<域名>.<公司/组织/个人名>.<项目/模块名>.spring.boot.autoconfigure`； 
* 自动配置类名命名规则：项目/模块名 + `AutoConfiguration`； 
* 配置参数类名命名规则：项目/模块名 + `Properties`。

编写自动配置类：

```java
@Configuration
@EnableConfigurationProperties(HelloProperties.class) // 使用配置参数类进行自动配置功能
@ConditionalOnMissingBean(HelloAutoConfiguration.class) // 如果容器内没有此组件则注册此组件
public class HelloAutoConfiguration {
    private final HelloProperties properties;
    // 通过构造器注入
    public HelloAutoConfiguration(HelloProperties properties) {
        this.properties = properties;
    }
    @Bean
    @ConditionalOnMissingBean // 如果容器内没有此组件则注册此组件
    public HelloService helloService() {
        return new HelloSerivce(properties.getMessage());
    }
}
```

配置参数类：

```java
@ConfigurationProperties(prefix = HelloProperties.HELLO_PREFIX)
public class HelloProperties {
    public static final String HELLO_PREFIX = "hello"; // 配置属性的前缀
    // 需要配置的属性，可以初始化一个值作为默认值
    /**
     * （属性的注释。如果指定注释，则配置此属性的时候idea会有提示）
     */
    private String message = "Hello World!";
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
```

编写`spring.factories`：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  pers.dyj123.hello.spring.boot.autoconfigure.HelloAutoConfiguration
```

依赖：

```xml
<dependencies>
    <dependency>
        <groupId>pers.dyj123</groupId>
        <artifactId>dyj123-hello</artifactId>
        <version>1.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
        <version>2.4.5</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <version>2.4.5</version>
    </dependency>
</dependencies>
```

### 3.3 starter 启动器包

starter 启动器包负责引入代码包和 autoconfigure 自动配置包的依赖：

```xml
<dependencies>
    <dependency>
        <groupId>pers.dyj123</groupId>
        <artifactId>dyj123-hello</artifactId>
        <version>1.0</version>
    </dependency>
    <dependency>
        <groupId>pers.dyj123</groupId>
        <artifactId>dyj123-hello-spring-boot-autoconfigure</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

## 4. 测试自定义模块 starter

首先要将上述三个包从前往后顺序执行清理(`mvn clean`)、安装(`mvn install`)：

1. 代码包
2. autoconfigure 自动配置包
3. starter 启动器包

全部按顺序安装完成后，创建一个新的 SpringBoot 应用用于测试刚才编写的自定义模块 starter。

导入依赖：

```xml
<dependencies>
    <dependency>
        <groupId>pers.dyj123</groupId>
        <artifactId>dyj123-hello-spring-boot-starter</artifactId>
        <version>1.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

编写启动类：

```java
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
```

编写配置文件(`application.yaml`)：

```yaml
hello:
  message: 这是我的第一个自定义starter
```

运行应用，浏览器访问`/hello`请求，测试结果：

![自定义starter测试结果](H:\Documents Library\IDEA Projects\READMEimage\springboot_custom_starter_测试结果.JPG)

