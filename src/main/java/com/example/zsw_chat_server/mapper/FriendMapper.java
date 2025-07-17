package com.example.zsw_chat_server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

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
	
	@Select("""
		SELECT 
			u.uid,
			u.username,
			u.avatar,
			u.email,
			u.gender,
			u.last_login_at,
			u.created_at,
			f.friend_nickname,
			f.become_friend_time
		FROM friend f
		JOIN users u ON f.friend_uid = u.uid
		WHERE f.user_id = #{uid}
		  AND ${column} LIKE #{keyword}
	""")
	List<Map<String, Object>> searchMyFriends(
		@Param("uid") Long uid,
		@Param("column") String column, 
		@Param("keyword") String keyword
	);


	@Select("""
		SELECT 
			u.uid,
			u.username,
			u.avatar,
			u.email,
			u.gender,
			u.last_login_at,
			u.created_at
		FROM users u
		WHERE ${column} LIKE #{keyword}
	""")
	List<Map<String, Object>> searchAllUsersByKeyword(
		@Param("column") String column,
		@Param("keyword") String keyword
	);
	

}
