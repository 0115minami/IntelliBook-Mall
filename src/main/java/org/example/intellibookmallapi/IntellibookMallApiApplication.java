package org.example.intellibookmallapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.intellibookmallapi.mapper")
public class IntellibookMallApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntellibookMallApiApplication.class, args);
    }

}
