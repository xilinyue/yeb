package com.ztt.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author LiuSu
 * @create 2021/3/12 14:44
 */
@SpringBootApplication
@MapperScan("com.ztt.server.mapper")
public class YebApplication {

    public static void main(String args[]) {
        SpringApplication.run(YebApplication.class,args);
    }
}
