package com.example.zsw_chat_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;  // 动态查询条件构建器
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zsw_chat_server.entity.Friend;
import com.example.zsw_chat_server.entity.FriendRequest;
import com.example.zsw_chat_server.mapper.FriendMapper;
import com.example.zsw_chat_server.mapper.FriendRequestMapper;
import com.example.zsw_chat_server.service.FriendRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements FriendRequestService {

	private final FriendRequestMapper friendRequestMapper;

	@Autowired
	private FriendMapper friendMapper;

	public FriendRequestServiceImpl(FriendRequestMapper friendRequestMapper) {
		this.friendRequestMapper = friendRequestMapper;
	}

	/**
	 * 发起好友申请
	 * 
	 * @param fromUid 发起人 UID
	 * @param toUid   接收人 UID
	 * @return 是否成功
	 */
	@Override
	public boolean applyFriendRequest(Long fromUid, Long toUid) {
		// 检查参数
		if (fromUid == null || toUid == null) {
			return false;
		}
		// 自己不能申请自己为好友
		if (fromUid.equals(toUid)) {
			return false;
		}
		// 查询是否已经申请过且是 pending 状态
		QueryWrapper<FriendRequest> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("from_uid", fromUid)
			.eq("to_uid", toUid)
			.eq("status", 0);
		if (this.count(queryWrapper) > 0) {
			// 已经申请过，且对方未通过，则不能重复申请
			return false;
		}
		// 创建新的申请
		FriendRequest request = new FriendRequest();
		request.setFromUid(fromUid);
		request.setToUid(toUid);
		request.setStatus(0);
		request.setRequestTime(LocalDateTime.now());
		return this.save(request);
	}

	/**
	 * 获取待处理的好友申请
	 * 
	 * @param toUid 接收人 UID
	 * @return 待处理的好友申请列表
	 */
	@Override
	public List<Map<String, Object>> getPendingRequests(Long toUid) {
		return friendRequestMapper.getPendingRequestsByToUid(toUid);
	}

	/**
	 * 接受好友申请
	 * 
	 * @param requestId 请求ID
	 * @param currentUid 当前用户UID
	 * @return 是否成功
	 */
	@Transactional   // 事务注解 保证 添加好友 和 修改 申请记录 的 状态 要么都成功 要么都失败
	@Override
	public boolean acceptRequest(Long requestId, Long currentUid) {
		// 从 friend_request 表中获取 申请记录 的 那一项
		FriendRequest request = this.getById(requestId);

		// 校验合法性
		if (request == null || !request.getToUid().equals(currentUid) || request.getStatus() != 0) {
			return false;
		}

		// 修改 申请记录 的 状态 ( 0 -> 1 ) friend_request 表
		request.setStatus(1);
		request.setHandleTime(LocalDateTime.now());
		this.updateById(request);

		// 添加好友 ( 双向 )
		Friend f1 = new Friend();
		f1.setUserId(request.getFromUid());
		f1.setFriendUid(request.getToUid());
		f1.setCreateTime(LocalDateTime.now());

		Friend f2 = new Friend();
		f2.setUserId(request.getToUid());
		f2.setFriendUid(request.getFromUid());
		f2.setCreateTime(LocalDateTime.now());

		// 插入 好友 表
		friendMapper.insert(f1);
		friendMapper.insert(f2);

		return true;
	}

	/**
	 * 拒绝好友申请
	 * 
	 * @param requestId 请求ID
	 * @param currentUid 当前用户UID
	 * @return success or fail
	 */
	@Override
	public boolean rejectRequest(Long requestId, Long currentUid) {
		FriendRequest request = this.getById(requestId);

		if (request == null || !request.getToUid().equals(currentUid) || request.getStatus() != 0) {
			return false;
		}

		request.setStatus(2);
		request.setHandleTime(LocalDateTime.now());
		return this.updateById(request);
	}

	/**
	 * 获取好友列表
	 * 
	 * @param uid 当前用户UID
	 * @return 好友列表
	 */
	@Override
	public List<Map<String, Object>> getFriendList(Long uid) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getFriendList'");
	}
	
}
