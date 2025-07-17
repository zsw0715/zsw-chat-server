package com.example.zsw_chat_server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zsw_chat_server.entity.Friend;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
	
	@Select("""
		select
			u.uid,
			u.username,
			u.avatar,
			u.email,
			u.gender,
			u.last_login_at,
			u.created_at,
			f.friend_nickname,
			f.become_friend_time
		from friend f
		join users u on f.friend_uid = u.uid
		where f.user_id = #{uid}
	""")
	List<Map<String, Object>> getFriendListByUid(Long uid);
	

}
