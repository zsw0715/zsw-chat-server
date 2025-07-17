package com.example.zsw_chat_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;  // 动态查询条件构建器
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zsw_chat_server.entity.Friend;
import com.example.zsw_chat_server.mapper.FriendMapper;
import com.example.zsw_chat_server.service.FriendService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FriendServiceImp extends ServiceImpl<FriendMapper, Friend> implements FriendService {

	private final FriendMapper friendMapper;


	public FriendServiceImp(FriendMapper friendMapper) {
		this.friendMapper = friendMapper;
	}

		/**
	 * 获取好友列表
	 * 
	 * @param uid 当前用户UID
	 * @return 好友列表
	 */
	@Override
	public List<Map<String, Object>> getFriendList(Long uid) {
		return friendMapper.getFriendListByUid(uid);
	}

	/**
	 * 更新好友备注
	 * 
	 * @param userId 当前用户UID
	 * @param friendUid 好友UID
	 * @param remark 备注
	 * @return 是否成功
	 */
	@Override
	public boolean updateFriendRemark(Long userId, Long friendUid, String remark) {
		UpdateWrapper<Friend> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("user_id", userId)
			.eq("friend_uid", friendUid)
			.set("friend_nickname", remark);
		return friendMapper.update(updateWrapper) > 0;
	}

	/**
	 * 删除好友
	 * 
	 * @param userId 当前用户UID
	 * @param friendUid 好友UID
	 * @return 是否成功
	 */
	@Override
	public boolean deleteFriend(Long userId, Long friendUid) {
		QueryWrapper<Friend> wrapper = new QueryWrapper<>();
		wrapper.and(w -> 
			w.eq("user_id", userId).eq("friend_uid", friendUid)
			 .or()
			 .eq("user_id", friendUid).eq("friend_uid", userId)
		);
		return friendMapper.delete(wrapper) > 0;
	}

	@Override
	public List<Map<String, Object>> searchMyFriendByKeyword(Long uid, String type, String keyword) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'searchMyFriendByKeyword'");
	}

	@Override
	public List<Map<String, Object>> searchAllUsersExcludingFriends(Long uid, String type, String keyword) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'searchAllUsersExcludingFriends'");
	}


}
