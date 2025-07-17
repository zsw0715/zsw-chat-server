package com.example.zsw_chat_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zsw_chat_server.entity.Friend;

import java.util.List;
import java.util.Map;

public interface FriendService extends IService<Friend> {

	/**
	 * 获取好友列表
	 * 
	 * @param uid 当前用户UID
	 * @return 好友列表
	 */
	List<Map<String, Object>> getFriendList(Long uid);

	/**
	 * 更新好友备注
	 * 
	 * @param userId 当前用户UID
	 * @param friendUid 好友UID
	 * @param remark 备注
	 * @return 是否成功
	 */
	boolean updateFriendRemark(Long userId, Long friendUid, String remark);

	/**
	 * 删除好友
	 * 
	 * @param userId 当前用户UID
	 * @param friendUid 好友UID
	 * @return 是否成功
	 */
	boolean deleteFriend(Long userId, Long friendUid);
	
	List<Map<String, Object>> searchMyFriendByKeyword(Long uid, String type, String keyword);

	List<Map<String, Object>> searchAllUsersExcludingFriends(Long uid, String type, String keyword);
	
}
