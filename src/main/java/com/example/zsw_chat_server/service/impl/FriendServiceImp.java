package com.example.zsw_chat_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; // 动态查询条件构建器
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zsw_chat_server.entity.Friend;
import com.example.zsw_chat_server.mapper.FriendMapper;
import com.example.zsw_chat_server.service.FriendService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * @param userId    当前用户UID
	 * @param friendUid 好友UID
	 * @param remark    备注
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
	 * @param userId    当前用户UID
	 * @param friendUid 好友UID
	 * @return 是否成功
	 */
	@Override
	public boolean deleteFriend(Long userId, Long friendUid) {
		QueryWrapper<Friend> wrapper = new QueryWrapper<>();
		wrapper.and(w -> w.eq("user_id", userId).eq("friend_uid", friendUid)
				.or()
				.eq("user_id", friendUid).eq("friend_uid", userId));
		return friendMapper.delete(wrapper) > 0;
	}

	/**
	 * 搜索好友 ( 只搜索好友 ), 根据用户名或邮箱搜索
	 * 
	 * @param uid     当前用户UID
	 * @param type    搜索类型 ( username, email )
	 * @param keyword 搜索关键词
	 * @return 搜索结果
	 */
	@Override
	public List<Map<String, Object>> searchMyFriendByKeyword(Long uid, String type, String keyword) {
		if (!type.equals("email") && !type.equals("username")) {
			return new ArrayList<>();
		}
		String column = type.equals("email") ? "email" : "username";
		return friendMapper.searchMyFriends(uid, column, "%" + keyword + "%");
	}

	/**
	 * 搜索users表 , 根据用户名或邮箱搜索. 搜索结果永远是 好友在上，路人在下。如果没有好友，则路人在上
	 * 
	 * @param uid     当前用户UID
	 * @param type    搜索类型 ( username, email )
	 * @param keyword 搜索关键词
	 * @return 搜索结果
	 */
	@Override
	public List<Map<String, Object>> searchAllUsersByKeyword(Long uid, String type, String keyword) {
		if (!type.equals("email") && !type.equals("username")) {
			return new ArrayList<>();
		}

		// 获取所有好友（带 friend_nickname、become_friend_time）
		List<Map<String, Object>> allFriendList = friendMapper.getFriendListByUid(uid);

		// 从中筛选出关键词匹配的好友
		List<Map<String, Object>> filteredFriendList = new ArrayList<>();
		Set<Long> friendUidSet = new HashSet<>();
		for (Map<String, Object> friend : allFriendList) {
			String field = type.equals("email") ? (String) friend.get("email") : (String) friend.get("username");
			if (field != null && field.contains(keyword)) {
				friend.put("isFriend", true); // 标记是好友

				// 如果是自己，加上 isYourself 标记
				Long friendId = (Long) friend.get("uid");
				friend.put("isYourself", uid.equals(friendId));

				filteredFriendList.add(friend);
				friendUidSet.add((Long) friend.get("uid"));
			}
		}

		// 搜索 users 表中所有匹配关键词的用户
		String column = type.equals("email") ? "email" : "username";
		List<Map<String, Object>> allUsersList = friendMapper.searchAllUsersByKeyword(column, "%" + keyword + "%");

		// 从中排除已是好友的 UID
		List<Map<String, Object>> strangerList = new ArrayList<>();
		for (Map<String, Object> user : allUsersList) {
			Long userId = (Long) user.get("uid");
			if (!friendUidSet.contains(userId)) {
				user.put("isFriend", false); // 标记非好友

				// 如果是自己，加上 isYourself 标记
				user.put("isYourself", uid.equals(userId));
				
				strangerList.add(user);
			}
		}

		// 合并结果：好友优先显示
		List<Map<String, Object>> result = new ArrayList<>();
		result.addAll(filteredFriendList);
		result.addAll(strangerList);
		return result;
	}

}
