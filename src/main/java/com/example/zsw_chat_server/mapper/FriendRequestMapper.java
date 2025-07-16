package com.example.zsw_chat_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zsw_chat_server.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

	@Select("""
		SELECT 
			fr.id AS request_id,
			fr.from_uid,
			u.username AS from_username,
			u.avatar AS from_avatar,
			fr.request_time
		FROM friend_request fr
		JOIN users u ON fr.from_uid = u.uid
		WHERE fr.to_uid = #{toUid} AND fr.status = 0
		ORDER BY fr.request_time DESC
	""")
	List<Map<String, Object>> getPendingRequestsByToUid(Long toUid);

}
