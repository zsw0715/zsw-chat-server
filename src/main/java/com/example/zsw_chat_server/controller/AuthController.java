package com.example.zsw_chat_server.controller;

import com.example.zsw_chat_server.entity.User;
import com.example.zsw_chat_server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	// 注册
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		if (userService.register(user)) {
			return new ResponseEntity<>("注册成功", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("邮箱已存在", HttpStatus.BAD_REQUEST);
	}

	// 登录
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User loginRequest) {
		User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
		System.out.println(user);
		if (user != null) {
			// 以后再实现 JWT 认证
			return new ResponseEntity<>("登录成功", HttpStatus.OK);
		}
		return new ResponseEntity<>("邮箱或密码错误", HttpStatus.UNAUTHORIZED);
	}
	
}
