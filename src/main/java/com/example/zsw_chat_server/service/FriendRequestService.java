package com.example.zsw_chat_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zsw_chat_server.entity.FriendRequest;

public interface FriendRequestService extends IService<FriendRequest> {

	/**
	 * 发起好友申请
	 * 
	 * @param fromUid 发起人 UID
	 * @param toUid   接收人 UID
	 * @return 是否成功
	 */
	boolean applyFriendRequest(Long fromUid, Long toUid);
	
}
