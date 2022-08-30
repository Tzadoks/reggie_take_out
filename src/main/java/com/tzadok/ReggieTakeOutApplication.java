package com.tzadok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Slf4j
@SpringBootApplication
@ServletComponentScan
//开启事务控制
@EnableTransactionManagement
//开启缓存
@EnableCaching
public class ReggieTakeOutApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggieTakeOutApplication.class, args);
		log.info("项目启动成功");
	}

}
