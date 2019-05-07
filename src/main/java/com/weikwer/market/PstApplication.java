package com.weikwer.market;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.weikwer.market.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class PstApplication {

    public static void main(String[] args) {
        SpringApplication.run(PstApplication.class, args);
    }

}
