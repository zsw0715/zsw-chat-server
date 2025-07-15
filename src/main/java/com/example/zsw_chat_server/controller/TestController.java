package com.example.zsw_chat_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	// 连接测试
	@GetMapping("/api/test")
	public String test() {
		return "test success，连接成功";
	}

}
