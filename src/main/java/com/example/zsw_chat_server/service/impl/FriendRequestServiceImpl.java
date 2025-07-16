package com.example.zsw_chat_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;  // 动态查询条件构建器
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zsw_chat_server.entity.FriendRequest;
import com.example.zsw_chat_server.mapper.FriendRequestMapper;
import com.example.zsw_chat_server.service.FriendRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements FriendRequestService {

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
	
}
