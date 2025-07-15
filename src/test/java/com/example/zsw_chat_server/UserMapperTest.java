package com.example.zsw_chat_server;

import java.util.List;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.zsw_chat_server.entity.User;
import com.example.zsw_chat_server.mapper.UserMapper;

@SpringBootTest
public class UserMapperTest {
	@Autowired
	private UserMapper userMapper;

	// 测试查询所有用户
	@Test
	public void testSelectAll() {
		List<User> userList = userMapper.selectList(null);
		userList.forEach(System.out::println);
	}

	// 测试插入用户
	@Test
	public void testInsert() {
		User user = new User();
		user.setUid(1000000L);
		user.setUsername("Allan");
		user.setPassword("123456");
		user.setEmail("allan@test.com");
		user.setCreated_at(LocalDateTime.now());
		user.setLast_login_at(LocalDateTime.now());
		user.setGender(1);
		userMapper.insert(user);
	}

	// 测试查询用户 by id
	@Test
	public void testSelect() {
		User user = userMapper.selectById(1);
		System.out.println(user);
	}

	// 测试查询用户 by username
	@Test
	public void testSelectByUsername() {
		User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "Allan"));
		System.out.println(user);
	}

	// 测试查询用户 by email
	@Test
	public void testSelectByEmail() {
		User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, "allan@test.com"));
		System.out.println(user);
	}

	// 测试删除用户，所有用户
	@Test
	public void testDelete() {
		userMapper.delete(null);
	}

}
