package com.example.zsw_chat_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zsw_chat_server.entity.User;

public interface UserService extends IService<User> {
	// 根据邮箱查询用户
	User selectByEmail(String email);
	// 注册用户
	boolean register(User user); 
	// 登录
	User login(String email, String password);
}
