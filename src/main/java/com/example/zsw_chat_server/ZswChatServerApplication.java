package com.example.zsw_chat_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.zsw_chat_server.mapper")
public class ZswChatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZswChatServerApplication.class, args);
	}

}
