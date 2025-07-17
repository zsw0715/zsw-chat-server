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
import com.example.zsw_chat_server.util.JwtUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
	private final FriendRequestService friendRequestService;
	private final JwtUtil jwtUtil;

	public FriendController(FriendRequestService friendRequestService, JwtUtil jwtUtil) {
		this.friendRequestService = friendRequestService;
		this.jwtUtil = jwtUtil;
	}

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

	@GetMapping("/list")
	public ResponseEntity<?> getFriendList(HttpServletRequest request) {
		Long uid = jwtUtil.getUidFromRequest(request);
		if (uid == null) {
			return new ResponseEntity<>("未登录", HttpStatus.UNAUTHORIZED);
		}
	
		List<Map<String, Object>> friends = friendRequestService.getFriendList(uid);
		return new ResponseEntity<>(friends, HttpStatus.OK);
	}


}
