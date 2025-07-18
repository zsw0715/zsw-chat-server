package com.example.zsw_chat_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.example.zsw_chat_server.service.FriendRequestService;
import com.example.zsw_chat_server.service.FriendService;
import com.example.zsw_chat_server.util.JwtUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
	private final FriendRequestService friendRequestService;
	private final JwtUtil jwtUtil;
	private final FriendService friendService;
	
	public FriendController(FriendRequestService friendRequestService, JwtUtil jwtUtil, FriendService friendService) {
		this.friendRequestService = friendRequestService;
		this.jwtUtil = jwtUtil;
		this.friendService = friendService;
	}

	/**
	 * 发送好友申请
	 * 
	 * @param toUid 目标用户ID
	 * @param request 请求对象
	 * @return 好友申请发送成功或失败
	 */
	@PostMapping("/apply")
	public ResponseEntity<String> applyFriendRequest(@RequestParam Long toUid, HttpServletRequest request) {
		System.out.println(">>>> applyFriendRequest 被调用了");

		Long fromUid = jwtUtil.getUidFromRequest(request);
		if (fromUid == null) {
			return new ResponseEntity<>("用户未登录", HttpStatus.UNAUTHORIZED);
		}
		if (toUid == null) {
			return new ResponseEntity<>("目标用户ID不能为空", HttpStatus.BAD_REQUEST);
		}

		boolean success = friendRequestService.applyFriendRequest(fromUid, toUid);
		if (success) {
			return new ResponseEntity<>("好友申请发送成功", HttpStatus.OK);
		}
		return new ResponseEntity<>("好友申请发送失败", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 获取好友申请
	 * 
	 * @param request 请求对象
	 * @return 好友申请列表
	 */
	@GetMapping("/requests")
	public ResponseEntity<?> getFriendRequests(HttpServletRequest request) {
		System.out.println(">>>> getFriendRequests 被调用了");

		Long fromUid = jwtUtil.getUidFromRequest(request);
		if (fromUid == null) {
			return new ResponseEntity<>("用户未登录", HttpStatus.UNAUTHORIZED);
		}

		List<Map<String, Object>> pendingRequests = friendRequestService.getPendingRequests(fromUid);
		if (pendingRequests.isEmpty()) {
			return new ResponseEntity<>("没有待处理的好友申请", HttpStatus.OK);
		}

		return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
	}

	/**
	 * 接受好友申请
	 * 
	 * @param requestId 请求ID
	 * @param request 请求对象
	 * @return 好友申请接受成功或失败
	 */
    @PostMapping("/accept")
	public ResponseEntity<?> acceptRequest(@RequestParam Long requestId, HttpServletRequest request) {
		System.out.println(">>>> acceptRequest 被调用了");

		Long uid = jwtUtil.getUidFromRequest(request);
		if (uid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}

		boolean success = friendRequestService.acceptRequest(requestId, uid);
		return success ? new ResponseEntity<>("好友申请已接受", HttpStatus.OK) : new ResponseEntity<>("好友申请接受失败", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 拒绝好友申请
	 * 
	 * @param requestId 请求ID
	 * @param request 请求对象
	 * @return 好友申请拒绝成功或失败
	 */
	@PostMapping("/reject")
	public ResponseEntity<?> rejectRequest(@RequestParam Long requestId, HttpServletRequest request) {
		System.out.println(">>>> rejectRequest 被调用了");

		Long uid = jwtUtil.getUidFromRequest(request);
		if (uid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}

		boolean success = friendRequestService.rejectRequest(requestId, uid);
		return success ? new ResponseEntity<>("好友申请已拒绝", HttpStatus.OK) : new ResponseEntity<>("好友申请拒绝失败", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 获取好友列表
	 * 
	 * @param request 请求对象
	 * @return 好友列表
	 */
	@GetMapping("/list")
	public ResponseEntity<?> getFriendList(HttpServletRequest request) {
		Long uid = jwtUtil.getUidFromRequest(request);
		if (uid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}
	
		List<Map<String, Object>> friends = friendService.getFriendList(uid);
		return new ResponseEntity<>(friends, HttpStatus.OK);
	}

	/**
	 * 更新好友备注
	 * 
	 * @param friendUid 好友ID
	 * @param remark 备注
	 * @param request 请求对象
	 * @return 备注更新成功或失败
	 */
	@PostMapping("/remark")
	public ResponseEntity<?> updateRemark(@RequestParam Long friendUid, @RequestParam String remark, HttpServletRequest request) {
		Long userId = jwtUtil.getUidFromRequest(request);
		if (userId == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}
	
		boolean success = friendService.updateFriendRemark(userId, friendUid, remark);
		return success ? new ResponseEntity<>("备注更新成功", HttpStatus.OK)
					   : new ResponseEntity<>("备注更新失败", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 删除好友 ( 双向删除 )
	 * 
	 * @param friendUid 好友ID
	 * @param request 请求对象
	 * @return 删除成功或失败
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> deleteFriend(@RequestParam Long friendUid, HttpServletRequest request) {
		Long uid = jwtUtil.getUidFromRequest(request);
		if (uid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}
	
		boolean success = friendService.deleteFriend(uid, friendUid);
		return success ? new ResponseEntity<>("删除成功", HttpStatus.OK) : new ResponseEntity<>("删除失败", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 搜索好友
	 * 
	 * @param type 搜索类型 ( username, email )
	 * @param keyword 搜索关键词
	 * @param onlyFriend 是否只搜索好友
	 * @param request 请求对象
	 * @return 搜索结果
	 */
	@GetMapping("/search")
	public ResponseEntity<?> searchFriend(@RequestParam String type, @RequestParam String keyword, @RequestParam boolean onlyFriend, HttpServletRequest request) {
		Long uid = jwtUtil.getUidFromRequest(request);
		if (uid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}

		List<Map<String, Object>> result;
		if (onlyFriend) {
			result = friendService.searchMyFriendByKeyword(uid, type, keyword);
		} else {
			result = friendService.searchAllUsersByKeyword(uid, type, keyword);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 根据 toUid 获取 是否这个 人 是否已经点过 添加好友 按钮
	 * 
	 * @param toUid 目标用户ID
	 * @param request 请求对象
	 * @return 是否已经添加过
	 */
	@GetMapping("/is-friend-request-pending")
	public ResponseEntity<?> isFriendRequestPending(@RequestParam Long toUid, HttpServletRequest request) {
		Long fromUid = jwtUtil.getUidFromRequest(request);
		if (fromUid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}

		boolean isPending = friendRequestService.isFriendRequestPending(fromUid, toUid);
		return isPending ? new ResponseEntity<>("对方正在处理中", HttpStatus.OK) : new ResponseEntity<>("toUid 有问题，或者还没有发送好友请求", HttpStatus.OK);
	}


}
