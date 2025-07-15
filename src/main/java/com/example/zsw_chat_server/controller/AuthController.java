package com.example.zsw_chat_server.controller;

import com.example.zsw_chat_server.entity.User;
import com.example.zsw_chat_server.service.UserService;
import com.example.zsw_chat_server.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final JwtUtil jwtUtil;

	public AuthController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
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
	public ResponseEntity<?> login(@RequestBody User loginRequest) {
		User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
		System.out.println(user);
		if (user != null) {
			String token = jwtUtil.generateToken(user); // 生成令牌
			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("user", user);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return new ResponseEntity<>("邮箱或密码错误", HttpStatus.UNAUTHORIZED);
	}
	
}
