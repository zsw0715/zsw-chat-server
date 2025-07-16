package com.example.zsw_chat_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.example.zsw_chat_server.service.FriendRequestService;
import com.example.zsw_chat_server.util.JwtUtil;

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
			return new ResponseEntity<>("用户未登录", HttpStatus.BAD_REQUEST);
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
	
	
}
