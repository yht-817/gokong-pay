package com.pay.tool.gokongpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration    // 配置注解，自动在本类上下文加载一些环境变量信息
@EnableSwagger2   // 使swagger2生效
public class Swagger2 {
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("business-api")
                .select()   // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.basePackage("com.pay.tool.gokongpay.web"))
                //.paths(paths())
                //.apis(RequestHandlerSelectors.any())  // 对所有api进行监控
                .paths(PathSelectors.any())   // 对所有路径进行监控
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("集成Swagger文档")
                .termsOfServiceUrl("http://baidu.com")
                .description("集成Swagger文档用于测试各个API接口，更多详情和限流请访问<a href='#'>集成文档</a>。") //描述
                .license("License Version 2.0")//许可类型
                .licenseUrl("#")  //许可路径
                .contact(new Contact("yangzhuang", "", "ideacoding@163.com"))
                .version("2.0").build();
    }
}
