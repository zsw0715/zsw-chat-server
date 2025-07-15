package com.example.zsw_chat_server.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zsw_chat_server.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
	
}
