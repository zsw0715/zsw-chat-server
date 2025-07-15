package com.example.zsw_chat_server;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.zsw_chat_server.entity.User;
import com.example.zsw_chat_server.mapper.UserMapper;

@SpringBootTest
public class UserMapperTest {
	@Autowired
	private UserMapper userMapper;

	@Test
	public void testSelectAll() {
		List<User> userList = userMapper.selectList(null);
		userList.forEach(System.out::println);
	}

}
