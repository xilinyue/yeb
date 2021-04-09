package com.ztt.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * @author LiuSu
 * @create 2021/3/12 19:55
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定在那个包下面生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.ztt.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes()); //配置请求头信息
    }

    /**
     * APIInfo
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version("0.0.1")
                .title("ztt接口文档")
                .description("ztt接口文档")
                .contact(new Contact("ZTT", "localhost:9090/doc.html","ztt@love.com"))
                .build();
    }

    /**
     * 解决访问接口登录问题
     * @return
     */
    private List<ApiKey> securitySchemes(){
        // 设置请求头信息
        List<ApiKey> result = new ArrayList<>();
        // 参数：api key名字 ｛准备的key名字，value请求头｝
        result.add(new ApiKey("Authorization","Authorization","header"));
        return result;
    }

    // 2. 解决访问接口登录问题
    private List<SecurityContext> securityContexts() {
        // 设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath());
        return result;
    }

    // 3. 解决访问接口登录问题
    private SecurityContext getContextByPath() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex("/hello/.*"))
//                .forPaths(PathSelectors.regex("/register/.*"))
                .build();
    }

    // 4. 设置默认授权 - 解决访问接口登录问题
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", scopes));
        return result;
    }

}
