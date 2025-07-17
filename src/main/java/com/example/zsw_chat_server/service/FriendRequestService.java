package com.example.zsw_chat_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zsw_chat_server.entity.FriendRequest;

import java.util.List;
import java.util.Map;

public interface FriendRequestService extends IService<FriendRequest> {

	/**
	 * 发起好友申请
	 * 
	 * @param fromUid 发起人 UID
	 * @param toUid   接收人 UID
	 * @return 是否成功
	 */
	boolean applyFriendRequest(Long fromUid, Long toUid);

	/**
	 * 获取待处理的好友申请
	 * 
	 * @param toUid 接收人 UID
	 * @return 待处理的好友申请列表
	 */
	List<Map<String, Object>> getPendingRequests(Long toUid);

	/**
	 * 接受好友申请
	 * 
	 * @param requestId 请求ID
	 * @param currentUid 当前用户UID
	 * @return 是否成功
	 */
	boolean acceptRequest(Long requestId, Long currentUid);

	/**
	 * 拒绝好友申请
	 * 
	 * @param requestId 请求ID
	 * @param currentUid 当前用户UID
	 * @return success or fail
	 */
    boolean rejectRequest(Long requestId, Long currentUid);

	/**
	 * 获取好友列表
	 * 
	 * @param uid 当前用户UID
	 * @return 好友列表
	 */
	List<Map<String, Object>> getFriendList(Long uid);

}
