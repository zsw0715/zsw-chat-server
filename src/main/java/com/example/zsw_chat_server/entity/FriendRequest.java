package com.example.zsw_chat_server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

@Data
@TableName("friend_request")
public class FriendRequest implements Serializable{
	@TableId(type = IdType.AUTO)
    private Long id;

    private Long fromUid;              // 发起人
    private Long toUid;                // 接收人
    private Integer status;            // 0:待处理，1:接受，2:拒绝

	@TableField("request_time")
    private LocalDateTime requestTime;
	
	@TableField("handle_time")
    private LocalDateTime handleTime;
}
