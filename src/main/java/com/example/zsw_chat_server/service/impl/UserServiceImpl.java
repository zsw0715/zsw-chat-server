package com.example.zsw_chat_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zsw_chat_server.entity.User;
import com.example.zsw_chat_server.mapper.UserMapper;
import com.example.zsw_chat_server.service.UserService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User selectByEmail(String email) {
		// SELECT * FROM users WHERE email = ? LIMIT 1
		return lambdaQuery().eq(User::getEmail, email).oneOpt().orElse(null);
	}

	@Override
	public boolean register(User user) {
		// 检查邮箱是否已存在
		if (selectByEmail(user.getEmail()) != null) {
			return false;
		}
		// 加密密码
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// 设置创建时间
		user.setCreated_at(LocalDateTime.now());
		// 设置用户名 -- 默认是unnameed，用户可以之后修改
		user.setUsername("unnameed");
		// 设置gender -- 默认 2 未知
		user.setGender(2);
		// 调用 MyBatis - Plus 通用方法保存
		return save(user);
	}

	@Override
	public User login(String email, String password) {
		// 根据邮箱查询用户
		User user = selectByEmail(email);
		// 校验密码（BCrypt 自动匹配）
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			// 更新最后登录时间
			user.setLast_login_at(LocalDateTime.now());
			// 更新用户信息
			updateById(user);
			return user;
		}
		return null;
	}

}
